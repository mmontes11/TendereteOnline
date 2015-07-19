package es.udc.pojo.advhibernatetut.model.hrservice;

import java.util.List;

import es.udc.pojo.advhibernatetut.model.department.Department;
import es.udc.pojo.advhibernatetut.model.employee.Employee;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface HrService {

    public void createDepartment(Department department);

    public void createEmployee(Employee employee, Long departmentId)
        throws InstanceNotFoundException;

    public void setDepartmentDirector(Long departmentId, Long employeeId)
        throws InstanceNotFoundException;

    public void removeEmployee(Long employeeId)
        throws InstanceNotFoundException;

    public void removeDepartment(Long departmentId)
        throws InstanceNotFoundException;

    public List<Department> findAllDepartments();

    public List<Employee> findEmployeesByDepartmentId(Long departmentId)
        throws InstanceNotFoundException;

    public Employee findDepartmentDirector(Long departmentId)
        throws InstanceNotFoundException, NoDepartmentDirectorAssignedException;


}
