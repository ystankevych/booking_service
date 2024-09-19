package com.stankevych.bookingapp.service;

import static com.stankevych.bookingapp.model.Accommodation.Type;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stankevych.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.stankevych.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.stankevych.bookingapp.exception.EntityNotFoundException;
import com.stankevych.bookingapp.mapper.AccommodationMapper;
import com.stankevych.bookingapp.model.Accommodation;
import com.stankevych.bookingapp.repository.AccommodationRepository;
import com.stankevych.bookingapp.service.impl.AccommodationServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {
    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private PaymentService paymentService;

    @Spy
    private AccommodationMapper mapper = Mappers.getMapper(AccommodationMapper.class);

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Test
    @DisplayName("""
            Verify create accommodation with valid requestDto 
            returned correct responseDto""")
    void createAccommodation_ValidRequestDto_Ok() {
        AccommodationRequestDto request = new AccommodationRequestDto(
                "HOUSE", "Ukraine", "Family",
                List.of("Wi-Fi", "Conditioner"), BigDecimal.TEN, 2
        );

        var unsavedAccommodation = getDefaultAccommodation(null, Type.HOUSE,
                "Ukraine", "Family", List.of("Wi-Fi", "Conditioner"),
                BigDecimal.TEN, 2);

        Accommodation savedAccommodation = getDefaultAccommodation(1L,
                Type.HOUSE, "Ukraine", "Family",
                List.of("Wi-Fi", "Conditioner"), BigDecimal.TEN, 2);

        when(accommodationRepository.save(unsavedAccommodation))
                .thenReturn(savedAccommodation);

        AccommodationResponseDto expected = new AccommodationResponseDto(1L,
                "HOUSE", "Ukraine", "Family",
                List.of("Wi-Fi", "Conditioner"), BigDecimal.TEN, 2);

        AccommodationResponseDto actual = accommodationService.createAccommodation(request);

        assertAll("Grouped Assertions of AccommodationResponseDto",
                () -> assertEquals(expected, actual,
                        "%s must be equal to %s".formatted(actual, expected)),
                () -> assertIterableEquals(expected.amenities(), actual.amenities(),
                        "Expected AccommodationResponseDto contains all %s"
                                .formatted(expected.amenities())));
    }

    @Test
    @DisplayName("Get list of all Accommodations")
    void getAll_Accommodations_ReturnedListOfDto() {
        var accommodation = getDefaultAccommodation(1L, Type.HOUSE, "Ukraine",
                "Family", List.of("Wi-Fi", "Conditioner"), BigDecimal.TEN, 2);
        var pageable = PageRequest.of(0, 10);
        Page<Accommodation> page = new PageImpl<>(Collections.singletonList(accommodation),
                pageable, 1);
        List<AccommodationResponseDto> expected = Collections.singletonList(
                new AccommodationResponseDto(1L, "HOUSE", "Ukraine",
                        "Family", List.of("Wi-Fi", "Conditioner"),
                        BigDecimal.TEN, 2));

        when(accommodationRepository.findAll(pageable))
                .thenReturn(page);

        List<AccommodationResponseDto> actual = accommodationService.getAll(pageable);
        assertEquals(expected, actual, """
                List of accommodations %s was expected to be returned
                but was returned %s"""
                .formatted(expected, actual));
    }

    @Test
    @DisplayName("Verify get accommodation by valid id returned correct AccommodationResponseDto")
    void getAccommodationById_ValidId_Ok() {
        var accommodationFromDb = getDefaultAccommodation(1L, Type.HOUSE, "Ukraine",
                "Family", List.of("Wi-Fi", "Conditioner"), BigDecimal.TEN, 2);
        var expectedResponseDto = new AccommodationResponseDto(1L, "HOUSE",
                "Ukraine", "Family", List.of("Wi-Fi", "Conditioner"),
                BigDecimal.TEN, 2);

        when(accommodationRepository.findById(1L))
                .thenReturn(Optional.of(accommodationFromDb));

        var actual = accommodationService.getAccommodationById(1L);

        assertAll("Grouped Assertions of AccommodationResponseDto",
                () -> assertEquals(expectedResponseDto, actual),
                () -> assertEquals(expectedResponseDto.amenities(), actual.amenities()));
    }

    @ParameterizedTest(name = """
            Get accommodation by invalid id should throw an EntityNotFoundException""")
    @ValueSource(longs = {10L, -1L})
    void getAccommodationById_InvalidId_ShouldThrowAnException(Long id) {
        when(accommodationRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accommodationService.getAccommodationById(id),
                "Expected EntityNotFoundExceptionToBeThrown");
    }

    @Test
    @DisplayName("""
            Verify update accommodation by valid id and requestDto 
            return correct AccommodationResponseDto""")
    void updateAccommodationById_ValidIdAndRequestDto_Ok() {
        var accommodationRequestDto = new AccommodationRequestDto(Type.CONDO.name(), "Kyiv",
                "Big", List.of("Balcony"), BigDecimal.ONE, 3);
        var accommodationFromDb = getDefaultAccommodation(1L, Type.HOUSE, "Ukraine",
                "Family", new ArrayList<>(List.of("Wi-Fi", "Conditioner")),
                BigDecimal.TEN, 2);
        var updatedAccommodation = getDefaultAccommodation(1L, Type.CONDO,
                "Kyiv", "Big", List.of("Balcony"), BigDecimal.ONE, 3);

        var expectedResponseDto = new AccommodationResponseDto(1L, "CONDO",
                "Kyiv", "Big", List.of("Balcony"), BigDecimal.ONE, 3);

        when(accommodationRepository.findById(anyLong()))
                .thenReturn(Optional.of(accommodationFromDb));
        when(accommodationRepository.save(updatedAccommodation))
                .thenReturn(updatedAccommodation);

        AccommodationResponseDto actual = accommodationService
                .updateAccommodationById(1L, accommodationRequestDto);

        assertAll("Grouped Assertions of AccommodationResponseDto",
                () -> assertEquals(expectedResponseDto, actual),
                () -> assertEquals(expectedResponseDto.amenities(), actual.amenities()));
    }

    @Test
    @DisplayName("Verify error message when update accommodation with invalid id")
    void updateAccommodationById_InvalidIdA_ExceptionMessage() {
        var accommodationRequestDto = new AccommodationRequestDto("CONDO", "Kyiv",
                "Big", List.of("Balcony"), BigDecimal.ONE, 3);
        String expectedErrorMessage = "Can't find accommodation by id: '10'";

        when(accommodationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.updateAccommodationById(10L, accommodationRequestDto),
                "Expected EntityNotFoundException to be thrown");
        String actualErrorMessage = exception.getMessage();

        assertEquals(expectedErrorMessage, actualErrorMessage,
                "Expected error message should be " + expectedErrorMessage);
    }

    @Test
    @DisplayName("Verify delete accommodation by valid id")
    void deleteAccommodation_ByValidId_Ok() {
        var accommodation = getDefaultAccommodation(1L, Type.HOUSE,
                "Ukraine", "family", List.of("Wi-Fi", "Conditioner"),
                BigDecimal.TEN, 2);

        when(accommodationRepository.findById(anyLong()))
                .thenReturn(Optional.of(accommodation));

        accommodationService.deleteAccommodation(1L);
        verify(accommodationRepository).delete(accommodation);
    }

    private Accommodation getDefaultAccommodation(Long id,
                                                  Type type,
                                                  String address,
                                                  String size,
                                                  List<String> amenities,
                                                  BigDecimal dailyRate,
                                                  Integer availability) {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(id);
        accommodation.setType(type);
        accommodation.setAddress(address);
        accommodation.setSize(size);
        accommodation.setAmenities(amenities);
        accommodation.setDailyRate(dailyRate);
        accommodation.setAvailability(availability);
        return accommodation;
    }
}
