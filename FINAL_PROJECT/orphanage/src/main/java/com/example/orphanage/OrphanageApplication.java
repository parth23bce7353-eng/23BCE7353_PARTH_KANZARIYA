package com.example.orphanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SpringBootApplication
public class OrphanageApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrphanageApplication.class, args);
    }
}

@Document(collection = "departments")
class Department {
    @Id private String id;
    private String name;
    private int capacity;
    public Department() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}

@Document(collection = "staff")
class Staff {
    @Id private String id;
    private String name;
    private String role;
    private String departmentId;
    public Staff() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
}

@Document(collection = "orphans")
class Orphan {
    @Id private String id;
    private String name;
    private int age;
    private String departmentId;
    private String guardianStaffId;
    public Orphan() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getGuardianStaffId() { return guardianStaffId; }
    public void setGuardianStaffId(String guardianStaffId) { this.guardianStaffId = guardianStaffId; }
}

@Document(collection = "books")
class Book {
    @Id private String id;
    private String title;
    private String description;
    public Book() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

interface DepartmentRepository extends MongoRepository<Department, String> {}
interface StaffRepository extends MongoRepository<Staff, String> {}
interface OrphanRepository extends MongoRepository<Orphan, String> {}
interface BookRepository extends MongoRepository<Book, String> {}

@RestController
@RequestMapping("/api")
class OrphanageController {

    @Autowired private DepartmentRepository deptRepo;
    @Autowired private StaffRepository staffRepo;
    @Autowired private OrphanRepository orphanRepo;
    @Autowired private BookRepository bookRepo;

    @PostMapping("/departments")
    public Department createDepartment(@RequestBody Department d) { return deptRepo.save(d); }

    @GetMapping("/departments")
    public List<Department> getAllDepartments() { return deptRepo.findAll(); }

    @PostMapping("/staff")
    public Staff createStaff(@RequestBody Staff s) { return staffRepo.save(s); }

    @GetMapping("/staff")
    public List<Staff> getAllStaff() { return staffRepo.findAll(); }

    @PutMapping("/staff/{id}")
    public Staff updateStaff(@PathVariable String id, @RequestBody Staff details) {
        Staff s = staffRepo.findById(id).orElseThrow();
        s.setName(details.getName());
        s.setRole(details.getRole());
        s.setDepartmentId(details.getDepartmentId());
        return staffRepo.save(s);
    }

    @PostMapping("/orphans")
    public Orphan createOrphan(@RequestBody Orphan o) { return orphanRepo.save(o); }

    @GetMapping("/orphans")
    public List<Orphan> getAllOrphans() { return orphanRepo.findAll(); }

    @GetMapping("/orphans/{id}")
    public Orphan getOrphanById(@PathVariable String id) { return orphanRepo.findById(id).orElseThrow(); }

    @DeleteMapping("/orphans/{id}")
    public String deleteOrphan(@PathVariable String id) {
        orphanRepo.deleteById(id);
        return "Success";
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody Book b) { return bookRepo.save(b); }

    @GetMapping("/books")
    public List<Book> getAllBooks() { return bookRepo.findAll(); }

    @DeleteMapping("/database/drop")
    public String dropDatabase() {
        deptRepo.deleteAll();
        staffRepo.deleteAll();
        orphanRepo.deleteAll();
        bookRepo.deleteAll();
        return "Database Reset Successful";
    }
}