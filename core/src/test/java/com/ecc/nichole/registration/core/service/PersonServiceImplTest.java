package com.ecc.nichole.registration.core.service;

import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Name;
import com.ecc.nichole.registration.core.model.Person;
import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.model.dto.PersonDto;
import com.ecc.nichole.registration.core.model.dto.RoleDto;
import com.ecc.nichole.registration.core.repo.PersonRepository;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.impl.PersonServiceImpl;
import com.ecc.nichole.registration.core.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    private PersonDto personDto;
    private Person person;
    private List<Role> rolesList;
    private Role adminRole;
    private Role softwareEngineerRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Person> personList = new ArrayList<>();
        rolesList = new ArrayList<>();

        Name name = new Name.Builder()
            .firstName("Nichole")
            .lastName("Avañez")
            .build();

        ContactInformation contactInformation = new ContactInformation.Builder()
            .email("nichole.avanez@exist.com")
            .build();

        adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("Admin");

        softwareEngineerRole = new Role();
        softwareEngineerRole.setId(2L);
        softwareEngineerRole.setName("Software Engineer");

        rolesList.add(adminRole);
        rolesList.add(softwareEngineerRole);

        person = new Person.Builder()
            .id(1L)
            .name(name)
            .gwa(1.25)
            .contactInformation(contactInformation)
            .roles(new HashSet<>(rolesList))
            .build();

        personDto = personServiceImpl.toDto(person);

        personList.add(person);

        name = new Name.Builder()
            .firstName("Anne")
            .lastName("Marie")
            .build();

        contactInformation = new ContactInformation.Builder()
            .email("anne.marie@exist.com")
            .build();


        Person newPerson = new Person.Builder()
            .id(2L)
            .name(name)
            .gwa(1.50)
            .contactInformation(contactInformation)
            .roles(new HashSet<>(rolesList))
            .build();

        personList.add(newPerson);

        when(personRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(personList));
        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));
        when(roleRepository.findByUuid(adminRole.getUuid())).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByUuid(softwareEngineerRole.getUuid())).thenReturn(Optional.of(softwareEngineerRole));

        doAnswer(invocation -> person).when(personRepository).save(person);
    }

    @Test
    void shouldCreatePerson() {
        personServiceImpl.create(personDto);

        assertNotNull(person, "Person should not be null.");
    }

    @Test
    void shouldGetPerson() {
        when(personServiceImpl.getByUuid(person.getUuid())).thenReturn(Optional.ofNullable(personDto));
        Optional<PersonDto> retrievedPerson = personServiceImpl.getByUuid(person.getUuid());

        assertTrue(retrievedPerson.isPresent(), "A person should be retrieved.");
        assertEquals(person.getId(), retrievedPerson.get().getId(), "The retrieved person's ID should match.");
    }

    @Test
    void shouldNotGetPerson() {
        when(personServiceImpl.getByUuid(person.getUuid())).thenReturn(Optional.empty());
        Optional<PersonDto> retrievedPerson = personServiceImpl.getByUuid(person.getUuid());

        assertTrue(retrievedPerson.isEmpty(), "No person should be retrieved.");
    }

    @ParameterizedTest
    @CsvSource({
            "gwa, asc",
            "id, desc"
    })
    void shouldGetAllPersons(String property, String orderBy) {
        Pageable pageable = PageRequest.of(0, 10);

        List<PersonDto> returnedList = personServiceImpl.getAll(property, orderBy, pageable);

        assertNotNull(returnedList, "The returned list should not be null.");
    }

    @Test
    void shouldGetAllRoles() {
        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));

        Pageable pageable = PageRequest.of(0, 10);
        List<RoleDto> returnedSet = personServiceImpl.getAllRoles(person.getUuid(), pageable);

        assertNotNull(returnedSet, "The returned set should not be null.");
    }

    @Test
    void shouldUpdatePerson() {
        Name updatedName = mock(Name.class);

        ContactInformation updatedContactInformation = mock(ContactInformation.class);

        PersonDto updatedPerson = personServiceImpl.toDto(new Person.Builder()
            .id(1L)
            .name(updatedName)
            .contactInformation(updatedContactInformation)
            .roles(new HashSet<>(Arrays.asList(adminRole, softwareEngineerRole)))
            .build());

        Optional<PersonDto> retrievedPerson = personServiceImpl.update(person.getUuid(), updatedPerson);

        assertTrue(retrievedPerson.isPresent(), "The retrieved person is not present.");
        assertEquals(person.getId(), retrievedPerson.get().getId(), "The retrieved person's ID should match.");
        assertEquals(person.getName().getFirstName(), retrievedPerson.get().getName().getFirstName(), "The retrieved person's name should match.");
        assertEquals(person.getContactInformation(), retrievedPerson.get().getContactInformation(), "The retrieved person's contact information should match.");

        verify(personRepository).findByUuid(person.getUuid());
        verify(roleRepository).findByUuid(adminRole.getUuid());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldPatchContactInformation() {
        ContactInformation updatedContactInfo = new ContactInformation.Builder()
            .email("new@example.com")
            .mobileNumber("9876543210")
            .landline("654321")
            .build();

        Person updatedPerson = person.toBuilder()
            .contactInformation(updatedContactInfo)
            .build();

        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        Optional<PersonDto> result = personServiceImpl.patchContactInformation(person.getUuid(), updatedContactInfo);

        assertTrue(result.isPresent());
        assertEquals("new@example.com", result.get().getContactInformation().getEmail());
        assertEquals("9876543210", result.get().getContactInformation().getMobileNumber());
        assertEquals("654321", result.get().getContactInformation().getLandline());

        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldNotPatchContactInformation() {
        ContactInformation updatedContactInfo = mock(ContactInformation.class);

        Person updatedPerson = person.toBuilder()
            .contactInformation(updatedContactInfo)
            .build();

        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.empty());
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        Optional<PersonDto> result = personServiceImpl.patchContactInformation(person.getUuid(), updatedContactInfo);

        assertFalse(result.isPresent(), "No person should be retrieved for contact information patch");
    }

    @Test
    void shouldPatchRoles() {
        when(roleRepository.findAllById(anySet())).thenReturn(new ArrayList<>(rolesList));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Optional<PersonDto> updatedPerson = personServiceImpl.patchRoles(person.getUuid(),
            rolesList.stream().map(role -> new RoleServiceImpl().toDto(role)).collect(Collectors.toList()));

        assertTrue(updatedPerson.isPresent());

        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldNotPatchRolesForNonExistingPerson() {
        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.empty());
        when(roleRepository.findAllById(anySet())).thenReturn(new ArrayList<>(rolesList));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Optional<PersonDto> updatedPerson = personServiceImpl.patchRoles(person.getUuid(),
            rolesList.stream().map(role -> new RoleServiceImpl().toDto(role)).collect(Collectors.toList()));

        assertFalse(updatedPerson.isPresent());
    }

    @Test
    void shouldNotPatchRolesForInvalidRoles() {
        when(personRepository.findByUuid(person.getUuid())).thenReturn(Optional.of(person));
        when(roleRepository.findAll()).thenReturn(rolesList);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            personServiceImpl.patchRoles(person.getUuid(),
                rolesList.stream().map(role -> new RoleServiceImpl().toDto(role)).collect(Collectors.toList()))
        );

        assertTrue(exception.getMessage().contains("Cannot update person's role for non-existing IDs:"), "Exception should list the invalid role IDs");

        verify(personRepository).findByUuid(person.getUuid());
        verify(roleRepository).findAllById(Set.of(1L, 2L));
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void shouldDelete() {
        when(personRepository.existsByUuid(person.getUuid())).thenReturn(true);
        doNothing().when(personRepository).deleteByUuid(person.getUuid());

        boolean result = personServiceImpl.delete(person.getUuid());

        assertTrue(result);
        verify(personRepository).deleteByUuid(person.getUuid());
    }

    @Test
    void shouldNotDelete() {
        when(personRepository.existsByUuid(person.getUuid())).thenReturn(false);

        boolean result = personServiceImpl.delete(person.getUuid());

        assertFalse(result);
        verify(personRepository, never()).deleteById(anyLong());
    }
}