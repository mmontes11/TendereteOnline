package es.udc.pojo.advhibernatetut.model.department;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import es.udc.pojo.advhibernatetut.model.employee.Employee;

@Entity
public class Department {

    private Long departmentId;
    private String name;
    private Calendar creationDate;
    private Set<Employee> employees = new HashSet<Employee>();
    private Employee director;
    private long version;

    public Department() {}

    public Department(String name, Calendar creationDate) {

        /**
         * NOTE: "departmentId" *must* be left as "null" since its value
         * is automatically generated.
         */
        this.name = name;
        this.creationDate = creationDate;

    }

    @Column(name = "depId")
    @SequenceGenerator(                // It only takes effect for
         name="DepartmentIdGenerator", // databases providing identifier
         sequenceName="DepartmentSeq") // generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="DepartmentIdGenerator")
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.DATE)
    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    @OneToMany(mappedBy = "department")
    public Set<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {

        employees.remove(employee);
        employee.setDepartment(null);

        if (employee == director) {
            director = null;
        }

    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "dirId")
    public Employee getDirector() {
        return director;
    }

    public void setDirector(Employee director) {
        this.director = director;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
