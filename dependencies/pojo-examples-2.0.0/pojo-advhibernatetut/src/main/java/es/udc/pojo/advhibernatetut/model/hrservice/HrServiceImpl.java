package es.udc.pojo.advhibernatetut.model.hrservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pojo.advhibernatetut.model.department.Department;
import es.udc.pojo.advhibernatetut.model.department.DepartmentDao;
import es.udc.pojo.advhibernatetut.model.employee.Employee;
import es.udc.pojo.advhibernatetut.model.employee.EmployeeDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("hrService")
@Transactional
public class HrServiceImpl implements HrService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    public void createDepartment(Department department) {
        departmentDao.save(department);
    }

    public void createEmployee(Employee employee, Long departmentId)
            throws InstanceNotFoundException {

        Department department =
            (Department) departmentDao.find(departmentId);

        department.addEmployee(employee);
        employeeDao.save(employee);

    }

    public void setDepartmentDirector(Long departmentId, Long employeeId)
            throws InstanceNotFoundException {

        Department department =
            (Department) departmentDao.find(departmentId);
        Employee employee = (Employee) employeeDao.find(employeeId);

        department.setDirector(employee);

    }

    public void removeEmployee(Long employeeId)
        throws InstanceNotFoundException {

        Employee employee = employeeDao.find(employeeId);
        Department department = employee.getDepartment();

        /* Remove the employee from her/his department. */
        if (department != null) {
            department.removeEmployee(employee);
        }

        employeeDao.remove(employeeId);

    }

    public void removeDepartment(Long departmentId)
        throws InstanceNotFoundException {

        Department department = departmentDao.find(departmentId);

        for (Employee e : department.getEmployees()) {
            employeeDao.remove(e.getEmployeeId());
        }

        departmentDao.remove(departmentId);

    }

    public List<Department> findAllDepartments() {
        return departmentDao.findAll();
    }

    public List<Employee> findEmployeesByDepartmentId(Long departmentId)
        throws InstanceNotFoundException {

        /* Check departmentId. */
        departmentDao.find(departmentId);

        /* Return employees. */
        return employeeDao.findByDepartmentId(departmentId);

    }

    public Employee findDepartmentDirector(Long departmentId) throws
        InstanceNotFoundException, NoDepartmentDirectorAssignedException {

        Department department =
            (Department) departmentDao.find(departmentId);
        Employee director = department.getDirector();

        if (director == null) {
            throw new NoDepartmentDirectorAssignedException(departmentId);
        } else {
            return director;
        }

    }

}
