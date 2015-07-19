package es.udc.pojo.advhibernatetut.test.model;

import static es.udc.pojo.advhibernatetut.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pojo.advhibernatetut.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pojo.advhibernatetut.demo.DemoUtil;
import es.udc.pojo.advhibernatetut.model.department.Department;
import es.udc.pojo.advhibernatetut.model.department.DepartmentDao;
import es.udc.pojo.advhibernatetut.model.employee.Employee;
import es.udc.pojo.advhibernatetut.model.hrservice.HrService;
import es.udc.pojo.advhibernatetut.model.hrservice.NoDepartmentDirectorAssignedException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * It only includes two test cases for demonstration purposes.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE})
@Transactional
public class HrServiceTest {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private HrService hrService;

    @BeforeClass
    public static void createDemoData() throws Throwable {
        DemoUtil.initialize(
            new String[] {SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE});
        DemoUtil.createDemoData();
    }

    @AfterClass
    public static void removeDemoData() throws Throwable {
        DemoUtil.removeDemoData();
    }

    @Test
    public void testFindAllDepartments() throws InstanceNotFoundException {

        List<Department> departments = hrService.findAllDepartments();

        assertTrue(departments.size() == 4);
        assertTrue(departments.contains(
            departmentDao.find(DemoUtil.getIctDepartmentId())));
        assertTrue(departments.contains(
            departmentDao.find(DemoUtil.getEsDepartmentId())));
        assertTrue(departments.contains(
            departmentDao.find(DemoUtil.getCsDepartmentId())));
        assertTrue(departments.contains(
            departmentDao.find(DemoUtil.getNuDepartmentId())));

    }

    @Test
    public void testFindDepartmentDirector()
        throws InstanceNotFoundException,
               NoDepartmentDirectorAssignedException {

        Long departmentId = DemoUtil.getIctDepartmentId();
        Employee director = hrService.findDepartmentDirector(departmentId);

        assertEquals(departmentDao.find(departmentId).getDirector(), director);

    }

}
