package com.ecc.nichole.registration.core.service;

import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Name;
import com.ecc.nichole.registration.core.model.Person;
import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.repo.PersonRepository;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    private Person person;
    private List<Person> personList;
    private List<Role> rolesList;
    private Role adminRole;
    private Role softwareEngineerRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personList = new ArrayList<>();
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

        personList.add(person);

        name = new Name.Builder()
                .firstName("Anne")
                .lastName("Marie")
                .build();

        contactInformation = new ContactInformation.Builder()
                .email("anne.marie@exist.com")
                .build();


        person = new Person.Builder()
                .id(2L)
                .name(name)
                .gwa(1.50)
                .contactInformation(contactInformation)
                .roles(new HashSet<>(rolesList))
                .build();

        personList.add(person);
    }

    @Test
    void shouldCreatePerson() {
        personServiceImpl.create(person);

        assertNotNull(person, "Person should not be null.");
    }

    @ParameterizedTest
    @ValueSource( longs = {1L, 2L})
    void shouldGetPerson(Long id) {
        when(personServiceImpl.getById(id)).thenReturn(Optional.ofNullable(person));
        Optional<Person> retrievedPerson = personServiceImpl.getById(id);

        assertTrue(retrievedPerson.isPresent(), "A person should be retrieved.");
        assertEquals(person.getId(), retrievedPerson.get().getId(), "The retrieved person's ID should match.");
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, 4L})
    void shouldNotGetPerson(Long id) {
        when(personServiceImpl.getById(id)).thenReturn(Optional.empty());
        Optional<Person> retrievedPerson = personServiceImpl.getById(id);

        assertTrue(retrievedPerson.isEmpty(), "No person should be retrieved.");
    }

    @ParameterizedTest
    @CsvSource({
            "gwa, asc",
            "id, desc"
    })
    void shouldGetAllPersons(String property, String orderBy) {
        when(personServiceImpl.getAll(property, orderBy)).thenReturn(personList);
        List<Person> returnedList = personServiceImpl.getAll(property, orderBy);

        assertNotNull(returnedList, "The returned list should not be null.");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void shouldGetAllRoles(Long id) {
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        List<Role> returnedSet = personServiceImpl.getAllRoles(id);

        assertNotNull(returnedSet, "The returned set should not be null.");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void shouldUpdatePerson(Long id) {
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(softwareEngineerRole));

        Name updatedName = mock(Name.class);

        ContactInformation updatedContactInformation = mock(ContactInformation.class);

        Person updatedPerson = new Person.Builder()
                .id(id)
                .name(updatedName)
                .contactInformation(updatedContactInformation)
                .roles(new HashSet<>(Arrays.asList(adminRole, softwareEngineerRole)))
                .build();

        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);
        Optional<Person> retrievedPerson = personServiceImpl.update(id, updatedPerson);

        assertTrue(retrievedPerson.isPresent(), "The retrieved person is not present.");
        assertEquals(updatedPerson.getId(), retrievedPerson.get().getId(), "The retrieved person's ID should match.");
        assertEquals(updatedPerson.getName(), retrievedPerson.get().getName(), "The retrieved person's name should match.");
        assertEquals(updatedPerson.getContactInformation(), retrievedPerson.get().getContactInformation(), "The retrieved person's contact information should match.");

        verify(personRepository).findById(id);
        verify(roleRepository).findById(id);
        verify(personRepository).save(any(Person.class));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void shouldPatchContactInformation(Long id) {
        ContactInformation updatedContactInfo = new ContactInformation.Builder()
                .email("new@example.com")
                .mobileNumber("9876543210")
                .landline("654321")
                .build();

        Person updatedPerson = person.toBuilder()
                .contactInformation(updatedContactInfo)
                .build();

        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        Optional<Person> result = personServiceImpl.patchContactInformation(id, updatedContactInfo);

        assertTrue(result.isPresent());
        assertEquals("new@example.com", result.get().getContactInformation().getEmail());
        assertEquals("9876543210", result.get().getContactInformation().getMobileNumber());
        assertEquals("654321", result.get().getContactInformation().getLandline());

        verify(personRepository).save(any(Person.class));
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, 4L})
    void shouldNotPatchContactInformation(Long id) {
        ContactInformation updatedContactInfo = mock(ContactInformation.class);

        Person updatedPerson = person.toBuilder()
                .contactInformation(updatedContactInfo)
                .build();

        when(personRepository.findById(id)).thenReturn(Optional.empty());
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        Optional<Person> result = personServiceImpl.patchContactInformation(id, updatedContactInfo);

        assertFalse(result.isPresent(), "No person should be retrieved for contact information patch");
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void shouldPatchRoles(Long id) {
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(roleRepository.findAllById(anySet())).thenReturn(new ArrayList<>(rolesList));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Optional<Person> updatedPerson = personServiceImpl.patchRoles(id, rolesList);

        assertTrue(updatedPerson.isPresent());

        verify(personRepository).save(any(Person.class));
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, 4L})
    void shouldNotPatchRolesForNonExistingPerson(Long id) {
        when(personRepository.findById(id)).thenReturn(Optional.empty());
        when(roleRepository.findAllById(anySet())).thenReturn(new ArrayList<>(rolesList));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Optional<Person> updatedPerson = personServiceImpl.patchRoles(id, rolesList);

        assertFalse(updatedPerson.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void shouldNotPatchRolesForInvalidRoles(Long id) {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(roleRepository.findAll()).thenReturn(rolesList);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                personServiceImpl.patchRoles(1L, rolesList)
        );

        assertTrue(exception.getMessage().contains("Cannot update person's role for non-existing IDs:"),
                "Exception should list the invalid role IDs");

        verify(personRepository).findById(1L);
        verify(roleRepository).findAllById(Set.of(1L, 2L));
        verify(personRepository, never()).save(any(Person.class));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void shouldDelete(Long id) {
        when(personRepository.existsById(id)).thenReturn(true);
        doNothing().when(personRepository).deleteById(id);

        boolean result = personServiceImpl.delete(id);

        assertTrue(result);
        verify(personRepository).deleteById(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {3L, 4L})
    void shouldNotDelete(Long id) {
        when(personRepository.existsById(id)).thenReturn(false);

        boolean result = personServiceImpl.delete(id);

        assertFalse(result);
        verify(personRepository, never()).deleteById(anyLong());
    }

}