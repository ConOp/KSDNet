package Classes;

public class UserFactory {
    public User UserIdentification(String userid) throws Exception{
        if (userid.charAt(0) == 'S') {
            StudentMapper sm = new StudentMapper();
            return sm;
        } else if (userid.charAt(0) == 'T') {
            TeacherMapper tm = new TeacherMapper();
            return tm;
        }
        else{
            throw new Exception("Only Students or Teachers can register");
        }

    }


}
