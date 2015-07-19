package es.udc.pojo.advhibernatetut.model.department;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface DepartmentDao extends GenericDao<Department, Long> {

    public List<Department> findAll();

}
