package es.udc.pojo.advhibernatetut.model.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.BatchSize;

import es.udc.pojo.advhibernatetut.model.department.Department;

@Entity
@BatchSize(size = 10)
public class Employee {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private int salary;
    private Department department;
    private long version;

    public Employee() {}

    public Employee(String firstName, String lastName, String position,
        int salary) {

        /**
         * NOTE: "employeeId" *must* be left as "null" since its value
         * is automatically generated.
         */
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;

    }

    @Column(name = "empId")
    @SequenceGenerator(              // It only takes effect for
         name="EmployeeIdGenerator", // databases providing identifier
         sequenceName="EmployeeSeq") // generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="EmployeeIdGenerator")
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "depId")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
