package es.udc.pojo.advhibernatetut.model.hrservice;

@SuppressWarnings("serial")
public class NoDepartmentDirectorAssignedException extends Exception {

    private Long departmentId;
    
    public NoDepartmentDirectorAssignedException(Long departmentId) {
        super("No director assigned for department = '" + departmentId + "'");
        this.departmentId = departmentId;
    }
    
    public Long getDepartmentId() {
        return departmentId;
    }

}
