package es.udc.pojo.advhibernatetut.model.department;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("departmentDao")
public class DepartmentDaoHibernate extends
        GenericDaoHibernate<Department, Long> implements DepartmentDao {

    @SuppressWarnings("unchecked")
    public List<Department> findAll() {
        return getSession().createQuery("SELECT d FROM Department d " +
            "ORDER BY d.name").list();
    }

}
