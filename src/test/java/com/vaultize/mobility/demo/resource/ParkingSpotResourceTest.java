package com.vaultize.mobility.demo.resource;

import com.vaultize.mobility.demo.model.ParkingReservation;
import com.vaultize.mobility.demo.model.ParkingSpot;
import com.vaultize.mobility.demo.model.User;
import com.vaultize.mobility.demo.repository.ParkingSpotRepository;
import com.vaultize.mobility.demo.repository.ReservationRepository;
import com.vaultize.mobility.demo.repository.UserRepository;
import com.vaultize.mobility.demo.resource.dto.response.ParkingSpotResponseDTO;
import com.vaultize.mobility.demo.service.ParkingSpotService;
import com.vaultize.mobility.demo.service.UserService;
import com.vaultize.mobility.demo.service.adapter.ParkingSpotResponseAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ParkingSpotResourceImpl.class)
public class ParkingSpotResourceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ParkingSpotService parkingSpotService;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;


    @MockBean
    ReservationRepository reservationRepository;

    @MockBean
    ParkingSpotRepository parkingSpotRepository;


    private User mockUser1 = new User();
    private User mockUser2 = new User();

    private ParkingSpot shivajiNagar = new ParkingSpot();
    private ParkingSpot wakad = new ParkingSpot();
    private ParkingSpot shanipar = new ParkingSpot();
    private ParkingSpot sahakarNagar = new ParkingSpot();

    private ParkingReservation shivajiNagarReservation = new ParkingReservation();
    private ParkingReservation sahakarNagarReservation = new ParkingReservation();

    private List<ParkingSpot> availableParkingList = new ArrayList<>();
    private List<ParkingSpot> reservedParkingList = new ArrayList<>();
    private List<ParkingSpotResponseDTO> availableParkingDTOList = new ArrayList<>();
    private List<ParkingSpotResponseDTO> reservedParkingDTOList = new ArrayList<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockUser1.setUserId(1);
        mockUser1.setMobile("9789898999");
        mockUser1.setName("Lionel Messi");

        mockUser2.setUserId(2);
        mockUser2.setMobile("9999999999");
        mockUser2.setName("Jordi Alba");


        // Shivaji Nagar
        shivajiNagar.setAddress("Shivaji Nagar");
        shivajiNagar.setLatitude((float) 18.526110);
        shivajiNagar.setLongitude((float) 73.844131);


        // wakad
        wakad.setAddress("Wakad");
        wakad.setLatitude((float) 18.596340);
        wakad.setLongitude((float) 73.771889);

        // Shanipar
        shanipar.setAddress("Shanipar");
        shanipar.setLatitude((float) 18.517230);
        shanipar.setLongitude((float) 73.853400);

        // Sahakar Nagar
        sahakarNagar.setAddress("Sahakar Nagar");
        sahakarNagar.setLatitude((float) 18.489480);
        sahakarNagar.setLongitude((float) 73.851870);

        // Reservation
        shivajiNagarReservation.setCost(20);
        shivajiNagarReservation.setParkingSpot(shivajiNagar);
        shivajiNagarReservation.setUser(mockUser1);
        mockUser1.getParkingReservations().add(shivajiNagarReservation);
        shivajiNagar.getParkingReservations().add(shivajiNagarReservation);

        sahakarNagarReservation.setCost(15);
        sahakarNagarReservation.setParkingSpot(sahakarNagar);
        sahakarNagarReservation.setUser(mockUser1);
        mockUser1.getParkingReservations().add(sahakarNagarReservation);
        sahakarNagar.getParkingReservations().add(sahakarNagarReservation);

        availableParkingList.add(shanipar);
        availableParkingDTOList = new ParkingSpotResponseAdapter().buildResponse(availableParkingList);

        reservedParkingList.add(shivajiNagar);
        reservedParkingList.add(sahakarNagar);
        reservedParkingDTOList = new ParkingSpotResponseAdapter().buildResponse(reservedParkingList);

        when(parkingSpotService.getAvailable()).thenReturn(availableParkingDTOList);
        when(parkingSpotService.getReserved()).thenReturn(reservedParkingDTOList);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testSuccessfulAvailableSpots() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ParkingSpotResourceImpl.REST_PARKINGS_ENDPOINT_PREFIX).
                accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
