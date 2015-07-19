package es.udc.pojo.advhibernatetut.demo;

import java.util.Calendar;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import es.udc.pojo.advhibernatetut.model.department.Department;
import es.udc.pojo.advhibernatetut.model.department.DepartmentDao;
import es.udc.pojo.advhibernatetut.model.employee.Employee;
import es.udc.pojo.advhibernatetut.model.employee.EmployeeDao;
import es.udc.pojo.advhibernatetut.model.util.CalendarUtil;

/**
 *  This class is reused both for testing and for creating/removing data from
 *  DemoServlet.
 */
public final class DemoUtil {

    private static PlatformTransactionManager transactionManager;
    private static EmployeeDao employeeDao;
    private static DepartmentDao departmentDao;

    private static Long ictDepartmentId;
    private static Long esDepartmentId;
    private static Long csDepartmentId;
    private static Long nuDepartmentId;

    public static void initialize(ApplicationContext context) {
        createBeans(context);
    }

    public static void initialize(String[] configLocations) {
        createBeans(new ClassPathXmlApplicationContext(configLocations));
    }

    public static Long getIctDepartmentId() {
        return ictDepartmentId;
    }

    public static Long getEsDepartmentId() {
        return esDepartmentId;
    }

    public static Long getCsDepartmentId(){
        return csDepartmentId;
    }

    public static Long getNuDepartmentId() {
        return nuDepartmentId;
    }

    public static void createDemoData() throws Throwable  {
        /*
         * IMPORTANT: to avoid problems, use only English characters in
         * Strings (in the source file).
         */

        TransactionStatus transactionStatus =
            transactionManager.getTransaction(null);

        try {

            /* ICT Department and its employees. */
            Department ict = new Department(
                "Department of Information and Communications Technologies",
                CalendarUtil.getDate(1, Calendar.FEBRUARY, 2000));
            Employee fbellas = new Employee("Fernando", "Bellas Permuy", "tu",
                200);
            Employee jrs = new Employee("Juan", "Raposo Santiago", "cd", 200);
            Employee mad = new Employee("Manuel", "Alvarez Diaz", "cd", 200);
            Employee jss = new Employee("Jose", "Soto Soto", "tu", 300);

            ict.addEmployee(fbellas);
            ict.addEmployee(jrs);
            ict.addEmployee(mad);
            ict.addEmployee(jss);
            ict.setDirector(jss);
            departmentDao.save(ict);
            employeeDao.save(fbellas);
            employeeDao.save(jrs);
            employeeDao.save(mad);
            employeeDao.save(jss);
            ictDepartmentId = ict.getDepartmentId();

            /* ES Department and its employees. */
            Department es = new Department(
                "Department of Electronics and Systems",
                CalendarUtil.getDate(1, Calendar.FEBRUARY, 1990));
            Employee arr = new Employee("Alberto", "Romero Romero", "tu", 220);
            Employee joo = new Employee("Jaime", "Ortega Ortega", "tu", 300);

            es.addEmployee(arr);
            es.addEmployee(joo);
            es.setDirector(joo);
            departmentDao.save(es);
            employeeDao.save(arr);
            employeeDao.save(joo);
            esDepartmentId = es.getDepartmentId();

            /* CS Department and its employees. */
            Department cs = new Department(
                "Department of Computer Science",
                CalendarUtil.getDate(1, Calendar.FEBRUARY, 1993));
            Employee sgg = new Employee("Sara", "Gonzalez Gonzalez", "cu", 300);

            cs.addEmployee(sgg);
            cs.setDirector(sgg);
            departmentDao.save(cs);
            employeeDao.save(sgg);
            csDepartmentId = cs.getDepartmentId();

            /* NU Department (without employees!!!). */
            Department nu = new Department("Department of Nothing Useful",
                CalendarUtil.getDate(1, Calendar.FEBRUARY, 1995));
            departmentDao.save(nu);
            nuDepartmentId = nu.getDepartmentId();

            transactionManager.commit(transactionStatus);

        } catch (Throwable e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }

    }

    public static void removeDemoData() throws Throwable {

        TransactionStatus transactionStatus =
            transactionManager.getTransaction(null);

        try {

            /* Remove all departments and their employees. */
            List<Department> departments = departmentDao.findAll();

            for (Department d : departments) {

                for (Employee e : d.getEmployees()) {
                    employeeDao.remove(e.getEmployeeId());
                }

                departmentDao.remove(d.getDepartmentId());

            }

            transactionManager.commit(transactionStatus);

        } catch (Throwable e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }

    }

    private static void createBeans(ApplicationContext context) {

        transactionManager = context.getBean(PlatformTransactionManager.class);
        employeeDao = context.getBean(EmployeeDao.class);
        departmentDao = context.getBean(DepartmentDao.class);

    }

}
