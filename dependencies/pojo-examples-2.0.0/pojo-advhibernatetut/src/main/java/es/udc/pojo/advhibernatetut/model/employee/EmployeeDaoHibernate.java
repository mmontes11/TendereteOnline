package es.udc.pojo.advhibernatetut.model.employee;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("employeeDao")
public class EmployeeDaoHibernate extends GenericDaoHibernate<Employee, Long>
    implements EmployeeDao {

    @SuppressWarnings("unchecked")
    public List<Employee> findByDepartmentId(Long departmentId) {
        return getSession().createQuery("SELECT e FROM Employee e " +
            "WHERE e.department.departmentId = :departmentId " +
            "ORDER BY e.lastName, e.firstName").
            setParameter("departmentId", departmentId).list();
    }

}
