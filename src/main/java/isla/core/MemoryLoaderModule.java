package isla.core;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class MemoryLoaderModule {

    private ArrayList<Person> peopleLibrary = new ArrayList<>();

    public void dataLoad(){
    // Main Memory Reader
        try {
            Scanner rawMem = new Scanner(new FileReader("IslaMem.txt"));
            StringBuilder result = new StringBuilder(rawMem.next());

            while(rawMem.hasNext()){
                result.append(" ").append(rawMem.next());
            }

            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(result.toString());
            JSONObject allData = (JSONObject) obj;
            JSONObject adminData = (JSONObject) allData.get("Admin");
            JSONObject userBio = (JSONObject) adminData.get("UserBios");

            String name = userBio.get("Name").toString();
            String birthday = userBio.get("Birthday").toString();
            String gender = userBio.get("Gender").toString();

            List<String> preferredName = new ArrayList<>();
            preferredName.addAll(0, GetRes(userBio, "Preferred Name"));

            HashMap<String, List<String>> ULikes = new HashMap<>();
            HashMap<String, List<String>> UDLikes = new HashMap<>();

            JSONObject Likes = (JSONObject) userBio.get("Like");
            ULikes.put("Food",  GetRes(Likes, "Food"));
            ULikes.put("Game",  GetRes(Likes, "Game"));
            ULikes.put("Watch",  GetRes(Likes, "Watch"));
            ULikes.put("Others", GetRes(Likes, "Others"));
            JSONObject Dislikes = (JSONObject) userBio.get("Dislike");
            UDLikes.put("Food", GetRes(Dislikes, "Food"));
            UDLikes.put("Others", GetRes(Dislikes, "Others"));

            Admin Darwin = new Admin(name, preferredName, gender, birthday, ULikes, UDLikes);

            //Personal info all loaded, now moving on to Connections
            JSONObject connections = (JSONObject) allData.get("Connections");
            JSONObject Fam = (JSONObject) connections.get("Family");
            String[] fam = Fam.toString().split(",");
            for (String element : fam) {
                int a = 0;
                Family.put(element.split(":")[a].replaceAll("[^a-zA-Z ]", ""),
                        element.split(":")[a+1].replaceAll("[^a-zA-Z ]", ""));
            }

            JSONObject friend = (JSONObject) connections.get("Friends");
            String[] fri = friend.toString().split(",");
            for (String element : fri) {
                int a = 0;
                Friends.put(element.split(":")[a].replaceAll("[^a-zA-Z ]", ""),
                        List.of(element.split(":")[a + 1].replaceAll("[^a-zA-Z _]", "")
                                .split("_")));
            }

            rawMem.close();
        } catch (FileNotFoundException | ParseException e) {
            System.out.println("*Memory data loading error*");
        }
    }

    /**
     * GetRes is a function made to facilitate importing memories
     *
     * @param JObj
     * @param Key
     * @return
     */
    public static List<String> GetRes(JSONObject JObj, String Key) {
        List<String> Value = new java.util.ArrayList<>(
                List.of(Arrays.toString(JObj.get(Key).toString().split(","))));
        Value.replaceAll(s -> s.replaceAll("[^a-zA-Z0-9-_ ]", ""));
        return Value;
    }

    // Main Memory Writer
    public static void MemWrite() {
        HashMap<String, List<String>> Friends = new HashMap<>();
        List<String> mate = new ArrayList<>();
        List<String> mateIn = new ArrayList<>();

        Friends.forEach((key, val) -> {
            mate.add(key);
            StringBuilder anw = new StringBuilder();
            for (String element : val) {
                anw.append(element).append("_");
            }
            mateIn.add(anw.toString());
        });

        JSONObject Friends = new JSONObject();
        for (int y = 0; y < mate.size(); y++) {
            Friends.put(mate.get(y), mateIn.get(y));
        }

        JSONObject Super = new JSONObject();
        JSONObject Familya = new JSONObject();
        JSONObject UserBio = new JSONObject();
        Family.forEach((key, val) -> {
            Familya.put(key, val);
        });
        JSONObject Likes = new JSONObject();
        JSONObject Dislikes = new JSONObject();
        JSONObject UserBios = new JSONObject();
        JSONObject Conn = new JSONObject();
        UserBios.put("Name", Bios.get("Name"));
        UserBios.put("Birthday", Bios.get("Birthday"));
        UserBios.put("Gender", Bios.get("Gender"));
        UserBios.put("Preferred Name", PerName);
        Likes.put("Food", ULikes.get("Food"));
        Likes.put("Game", ULikes.get("Game"));
        Likes.put("Watch", ULikes.get("Watch"));
        Likes.put("Others", ULikes.get("Others"));
        Dislikes.put("Food", UDlikes.get("Food"));
        Dislikes.put("Others", UDlikes.get("Others"));
        Conn.put("Family", Familya);
        Conn.put("Friends", Friends);

        UserBios.put("Like", Likes);
        UserBios.put("Dislike", Dislikes);
        UserBio.put("UserBios", UserBios);
        Super.put("Connections", Conn);
        Super.put("Creator", UserBio);

        try (FileWriter file = new FileWriter("IslaMem.txt", false)) {
            file.write(Super.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void addPerson(String name, String relation){
        if (Objects.equals(relation, "none")){
            peopleLibrary.add(new Person(name));
        } else {
            peopleLibrary.add(new Person(name, relation));
        }
    }

    // Creating Person class
    class Person{
        private String fullName;
        private String relation;
        private String gender;
        private int yearOfBirth;
        private List<String> likes = new ArrayList<>();
        private List<String> dislikes = new ArrayList<>();

        Person(String fullName, String relation, String gender, int yearOfBirth,
               List<String> likes, List<String> dislikes){
            this.fullName = fullName;
            this.relation = relation;
            this.yearOfBirth = yearOfBirth;
            this.gender = gender;
            this.likes = likes;
            this.dislikes = dislikes;
        }

        Person(String fullName, String relation, String gender, int yearOfBirth){
            this.fullName = fullName;
            this.relation = relation;
            this.yearOfBirth = yearOfBirth;
            this.gender = gender;
        }

        Person(String fullName, String relation, String gender){
            this.fullName = fullName;
            this.relation = relation;
            this.gender = gender;
        }

        Person(String fullName, String relation){
            this.fullName = fullName;
            this.relation = relation;
        }

        Person(String fullName){
            this.fullName = fullName;
        }

        void setRelation(String newRelation){
            this.relation = newRelation;
        }

        void setYearOfBirth(int birthOrAge){
            // If the number entered is over 200, then add the date as a year
            if (birthOrAge > 200){
                this.yearOfBirth = birthOrAge;
            } else {
                this.yearOfBirth = Integer.parseInt(TimeModule.yearOut()) - birthOrAge;
            }
        }

        String getFullName(){
            return fullName;
        }

        String getRelation(){
            return relation;
        }

        int getAge(){
            return Integer.parseInt(TimeModule.yearOut()) - yearOfBirth;
        }

        int getYearOfBirth() {
            return yearOfBirth;
        }
    }

    class Admin{
        private String fullName;
        private String gender;
        private String birthday;
        private int yearOfBirth;
        private int age;
        private List<String> nickName = new ArrayList<>();
        private HashMap<String, List<String>> ULikes = new HashMap<String, List<String>>();
        private HashMap<String, List<String>> UDlikes = new HashMap<String, List<String>>();

        Admin(String fullName, List<String> nickName, String gender, String birthday,
              HashMap<String, List<String>> ULikes, HashMap<String, List<String>> UDlikes){
            this.fullName = fullName;
            this.nickName = nickName;

            this.birthday = birthday;
            this.yearOfBirth = Integer.parseInt((birthday.split("-"))[0]);
            this.age = (Integer.parseInt(TimeModule.yearOut()) - yearOfBirth);

            this.gender = gender;
            this.ULikes = ULikes;
            this.UDlikes = UDlikes;
        }
    }

}
