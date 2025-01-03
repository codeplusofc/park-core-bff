package com.ParkCore.service;

import com.ParkCore.dto.attractionDto.AttractionConverter;
import com.ParkCore.dto.attractionDto.AttractionRequestDTO;
import com.ParkCore.dto.attractionDto.AttractionResponseDTO;
import com.ParkCore.enums.AttractionType;
import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.exceptions.NoContentException;
import com.ParkCore.model.Attraction;
import com.ParkCore.model.Event;
import com.ParkCore.repository.AttractionRepository;
import com.ParkCore.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static com.ParkCore.enums.AttractionType.ROLLER_COASTER;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private AttractionConverter attractionConverter;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private AttractionService attractionService;

    @Test
    public void shouldRegisterAttraction() {
        // Cenário
        var attractionRequest = mock(AttractionRequestDTO.class);
        var newAttraction = mock(Attraction.class);
        var responseDTO = mock(AttractionResponseDTO.class);

        // Configuração do DTO de requisição
        given(attractionRequest.getName()).willReturn("Rolling Coaster");
        given(attractionRequest.getDescription()).willReturn("Amazing Roller Coaster!");
        given(attractionRequest.getType()).willReturn(ROLLER_COASTER);
        given(attractionRequest.getMaximumCapacity()).willReturn(50);

        // Configuração da entidade
        given(newAttraction.getId()).willReturn(1L);
        given(newAttraction.getName()).willReturn("Rolling Coaster");
        given(newAttraction.getDescription()).willReturn("Amazing Roller Coaster!");
        given(newAttraction.getType()).willReturn(ROLLER_COASTER);
        given(newAttraction.getMaximumCapacity()).willReturn(50);

        // Configuração do conversor e repositório
        given(attractionConverter.toEntity(attractionRequest)).willReturn(newAttraction);
        given(attractionRepository.save(newAttraction)).willReturn(newAttraction);
        given(attractionConverter.toResponseDTO(newAttraction)).willReturn(responseDTO);

        // Configuração do DTO de resposta
        given(responseDTO.getId()).willReturn(1L);
        given(responseDTO.getName()).willReturn("Rolling Coaster");
        given(responseDTO.getDescription()).willReturn("Amazing Roller Coaster!");
        given(responseDTO.getType()).willReturn(ROLLER_COASTER);
        given(responseDTO.getMaximumCapacity()).willReturn(50);

        // Quando
        var result = attractionService.createAttraction(attractionRequest);

        // Validação
        assertNotNull(result); // Verifica se o resultado não é nulo
        assertEquals(1L, result.getId());
        assertEquals("Rolling Coaster", result.getName());
        assertEquals("Amazing Roller Coaster!", result.getDescription());
        assertEquals(ROLLER_COASTER, result.getType());
        assertEquals(50, result.getMaximumCapacity());
    }


    @Test
    public void shouldCheckNameIsUnique() {
        // Cenário: Nome da atração não está registrado no banco de dados
        var uniqueName = "Amazing Roller Coaster";

        var attractionRequest = mock(AttractionRequestDTO.class);

        // Simulação: existsByName retorna false para o nome informado
        given(attractionRepository.existsByName(uniqueName)).willReturn(false);

        // Simulação de dados do DTO
        given(attractionRequest.getName()).willReturn(uniqueName);

        // Criação de nova atração com o nome configurado
        var newAttraction = new Attraction();
        newAttraction.setName(uniqueName);

        // Simulação: save retorna a atração que está sendo passada
        given(attractionRepository.save(newAttraction)).willReturn(newAttraction);

        given(attractionConverter.toEntity(attractionRequest)).willReturn(newAttraction);

        // Quando: Tentamos criar uma nova atração
        var result = attractionService.createAttraction(attractionRequest);

        // Então: Verifica que o nome foi verificado como único antes da criação
        verify(attractionRepository).existsByName(uniqueName);  // Verifica se a verificação foi feita
        assertEquals(uniqueName, result.getName());              // Verifica o nome da atração
        assertNotNull(result.getId());                           // Verifica se o ID foi atribuído
    }


    @Test
    public void shouldReturnErrorNameNotUnique() {

        var attractionRequest = mock(AttractionRequestDTO.class);

        // Simula uma instância da classe Atracao, permitindo que métodos desta instância sejam mockados
        var attraction = mock(Attraction.class);

        // Simula o comportamento do método getNome() da atração para que sempre retorne "jhon"
        given(attraction.getName()).willReturn("jhon");

        // Simula o comportamento do atracaoRepository.save() para retornar a própria atração mockada
        given(attractionRepository.save(attraction)).willReturn(attraction);

        // Simula o comportamento do método existsByNome() para retornar true, indicando que o nome "jhon" já existe no repositório (nome não é único)
        given(attractionRepository.existsByName(attraction.getName())).willReturn(true);

        given(attractionConverter.toEntity(attractionRequest)).willReturn(attraction);

        // Verifica que, ao tentar criar uma nova atração com nome duplicado, o método createAtracao() deve lançar uma exceção do tipo BadRequestException
        thenThrownBy(() -> attractionService.createAttraction(attractionRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    public void shouldReturnTrueIfAssociatedEvent() {
        // Cenário: ID da atração
        var attractionId = 1L;

        // Simulação: O repositório retorna uma lista não vazia, indicando que há eventos associados à atração
        given(eventRepository.findByAttractionId(attractionId)).willReturn(List.of(mock(Event.class)));

        // Quando: O método hasAssociatedEvents é chamado com o ID da atração
        var result = attractionService.hasAssociatedEvents(attractionId);

        // Então: O resultado deve ser true, pois existem eventos associados
        assertTrue(result);
    }


    @Test
    public void shouldReturnErroIfDeletingAttractionWithAssociatedEvent() {
        // Cenário: ID da atração e eventos associados
        var attractionId = 1L;

        // Simulação: O repositório retorna uma lista não vazia, indicando que há eventos associados
        given(eventRepository.findByAttractionId(attractionId)).willReturn(List.of(mock(Event.class)));

        // Quando/Então: Tentar deletar a atração deve lançar uma exceção devido aos eventos associados
        thenThrownBy(() -> attractionService.deleteAttraction(attractionId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void shouldReturnListOfAttractions() {

        //Criacao de algumas atracoes para simular o retorno do repositorio
        var attraction1 = mock(Attraction.class);
        var attraction2 = mock(Attraction.class);

        var attractionDTO1 = mock(AttractionResponseDTO.class);
        var attractionDTO2 = mock(AttractionResponseDTO.class);

        //O repositorio retorna uma lista de atrações
        given(attractionRepository.findAll()).willReturn(List.of(attraction1, attraction2));

        given(attractionConverter.toResponseDTO(attraction1)).willReturn(attractionDTO1);
        given(attractionConverter.toResponseDTO(attraction2)).willReturn(attractionDTO2);

        //Quando o metodo listaAtracoes é chamado
        var result = attractionService.listAttractions();
        //entao verifica se o resultado contém as atracoes simuladas
        assertEquals(2, result.size());//deve conter 2 atracoes
        assertEquals(attractionDTO1, result.get(0));
        assertEquals(attractionDTO2, result.get(1));

    }

    @Test
    public void shouldReturnEmptyListIfThereAreNoAttractions() {
        given(attractionRepository.findAll()).willReturn(List.of());
        var result = attractionService.listAttractions();
        assertTrue(result.isEmpty());

    }

    @Test
    public void shouldReturnAttractionsOfTheSpecifiedType() {
        AttractionType type = ROLLER_COASTER;

        var attraction1 = mock(Attraction.class);
        var attraction2 = mock(Attraction.class);

        var attractionDTO1 = mock(AttractionResponseDTO.class);
        var attractionDTO2 = mock(AttractionResponseDTO.class);

        given(attractionRepository.findByType(type)).willReturn(List.of(attraction1, attraction2));

        given(attractionConverter.toResponseDTO(attraction1)).willReturn(attractionDTO1);
        given(attractionConverter.toResponseDTO(attraction2)).willReturn(attractionDTO2);


        var result = attractionService.findByType(type);

        assertEquals(2, result.size());
        assertEquals(attractionDTO1, result.get(0));
        assertEquals(attractionDTO2, result.get(1));
    }

    @Test
    public void shouldReturnExceptionIfAttractionOfTypeNotFound() {
        // Cenário: Tipo de atração sem nenhuma atração associada
        var type = AttractionType.ROLLER_COASTER;

        // Simulação: O repositório retorna uma lista vazia
        given(attractionRepository.findByType(type)).willReturn(List.of());

        // Quando/Então: Verifica se a exceção NoContentException é lançada ao não encontrar atrações do tipo especificado
        assertThatThrownBy(() -> attractionService.findByType(type))
                .isInstanceOf(NoContentException.class)
                .hasMessageContaining("No attractions of type 'ROLLER_COASTER' were found in the system.");

    }

    @Test
    public void shouldExceptionWhenDeletingAssociatedAttractionEvent() {
        // Cenário: Atração com ID 1 e eventos associados
        Long attractionId = 1L;

        // Simulação: Atração existe
        var attraction = new Attraction();
        attraction.setId(attractionId);
        given(attractionRepository.findById(attractionId)).willReturn(Optional.of(attraction));

        // Simulação: Eventos associados à atração
        var events = List.of(new Event()); // Suponha que exista um evento associado
        given(eventRepository.findByAttractionId(attractionId)).willReturn(events); // Ajuste para o repositório certo

        // Quando: Tentativa de deletar atração associada a eventos
        assertThatThrownBy(() -> attractionService.deleteAttraction(attractionId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Error deleting attraction: Attraction with ID 1 cannot be deleted because it is associated with one or more events.");

        // Então: Verifica que o método deleteById não foi chamado
        then(attractionRepository).should(never()).deleteById(attractionId);
    }

    @Test
    public void shouldDeleteAttractionWhenThereAreNoAssociatedEvents() {
        // Cenário: Atração com ID 1 e sem eventos associados
        Long attractionId = 1L;

        // Simulação: Atração existe
        var attraction = new Attraction();
        attraction.setId(attractionId);
        given(attractionRepository.findById(attractionId)).willReturn(Optional.of(attraction));

        // Simulação: Nenhum evento associado à atração

        given(eventRepository.findByAttractionId(attractionId)).willReturn(List.of());

        // Quando: Deletar atração
        attractionService.deleteAttraction(attractionId);

        // Então: O método delete(atracao) deve ser chamado
        then(attractionRepository).should().delete(attraction);

    }

}