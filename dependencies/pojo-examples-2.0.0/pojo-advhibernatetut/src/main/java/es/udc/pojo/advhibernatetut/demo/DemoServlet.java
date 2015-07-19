package es.udc.pojo.advhibernatetut.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import es.udc.pojo.advhibernatetut.model.department.Department;
import es.udc.pojo.advhibernatetut.model.employee.Employee;
import es.udc.pojo.advhibernatetut.model.hrservice.HrService;
import es.udc.pojo.advhibernatetut.model.hrservice.NoDepartmentDirectorAssignedException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@SuppressWarnings("serial")
public class DemoServlet extends HttpServlet {

    private HrService hrService;

    public void init(ServletConfig config) {

        WebApplicationContext context =
            WebApplicationContextUtils.getWebApplicationContext(
                config.getServletContext());

        hrService = context.getBean(HrService.class);
        DemoUtil.initialize(context);

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        /* Get value of parameter "userName". */
        String command = request.getParameter("command");

        /* Generate response. */
        response.setContentType("text/html; charset=ISO-8859-1");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>");
        out.println("Advnaced Hibernate Tutorial - Demo Servlet");
        out.println("</title></head>");
        out.println("<body>");

        if (command != null) {

            try {

                if (command.equals("createDemoData")) {
                    createDemoData(out);
                } else if (command.equals("removeDemoData")) {
                    removeDemoData(out);
                } else if (command.equals("removeEmployee")) {
                    removeEmployee(out, new Long(request.getParameter("id")));
                } else if (command.equals("removeDepartment")) {
                    removeDepartment(out, new Long(request.getParameter("id")));
                } else if (command.equals("findAllDepartments")) {
                    findAllDepartments(out);
                } else if (command.equals("findEmployeesByDepartment")) {
                    findEmployeesByDepartment(out,
                        new Long(request.getParameter("id")));
                } else if (command.equals("findDepartmentDirector")) {
                    findDepartmentDirector(out,
                        new Long(request.getParameter("id")));
                }

            } catch (Throwable e) {
                throw new ServletException(e);
            }

        }

        out.println("</body></html>");

        out.close();

    }

    private void createDemoData(PrintWriter out) throws Throwable {

        DemoUtil.createDemoData();
        out.println("Demo data created <br>");

    }

    private void removeDemoData(PrintWriter out) throws Throwable {

        DemoUtil.removeDemoData();
        out.println("Demo data removed <br>");

    }


    private void removeEmployee(PrintWriter out, Long employeeId)
        throws Throwable {

        out.println("***removeEmployee*** <br>");
        hrService.removeEmployee(employeeId);
    }

    private void removeDepartment(PrintWriter out, Long departmentId)
        throws Throwable {

        out.println("***removeDepartment*** <br>");
        hrService.removeDepartment(departmentId);
    }

    private void findAllDepartments(PrintWriter out) {

        out.println("***findAllDepartments*** <br>");
        printDepartments(out, hrService.findAllDepartments());

    }

    private void findEmployeesByDepartment(PrintWriter out,
        Long departmentId) throws InstanceNotFoundException {

        out.println("***findEmployeesByDepartment*** <br>");
        printEmployees(out,
            hrService.findEmployeesByDepartmentId(departmentId), false);

    }

    private void findDepartmentDirector(PrintWriter out, Long departmentId)
        throws InstanceNotFoundException,
        NoDepartmentDirectorAssignedException {

        out.println("***findDepartmentDirector*** <br>");
        printEmployee(out,
            hrService.findDepartmentDirector(departmentId), false);

    }

    private void printDeparment(PrintWriter out, Department d) {

        DateFormat dateFormater = DateFormat.getDateInstance();
        String creationDateAsString =
            dateFormater.format(d.getCreationDate().getTime());

        out.println(
            "departmentId = " + d.getDepartmentId() + " | " +
            "name = " + d.getName() + " | " +
            "creationDate = " + creationDateAsString);

        Employee director = d.getDirector();

        if (director != null) {
            out.println(" | director = " + director.getFirstName() +
                " " + director.getLastName());
        }

    }

    private void printDepartments(PrintWriter out,
        List<Department> departments) {

        for (Department d : departments) {
            printDeparment(out, d);
            out.println("<br>");
        }

    }

    private void printEmployee(PrintWriter out, Employee e,
        boolean printDepartmentName) {

        out.println(
            "employeeId = " + e.getEmployeeId() + " | " +
            "lastName = " + e.getLastName() + " | " +
            "firstName = " + e.getFirstName() + " | " +
            "position = " + e.getPosition() + " | " +
            "salary = " + e.getSalary());

        if (printDepartmentName) {
            out.print("| department = " + e.getDepartment().getName());
        }

    }

    private void printEmployees(PrintWriter out, List<Employee> employees,
        boolean printDepartmentName) {

        for (Employee e : employees) {
            printEmployee(out, e, printDepartmentName);
            out.println("<br>");
        }
    }

}
