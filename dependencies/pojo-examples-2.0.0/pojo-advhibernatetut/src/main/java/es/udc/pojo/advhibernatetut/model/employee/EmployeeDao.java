package es.udc.pojo.advhibernatetut.model.employee;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface EmployeeDao extends GenericDao<Employee, Long> {

    public List<Employee> findByDepartmentId(Long departmentId);

}
