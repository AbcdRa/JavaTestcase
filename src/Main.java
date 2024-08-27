import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

record Sportsman(String name, String command) {
}


class SportsmanContainer {
    private final List<Sportsman> sportsmanList = new ArrayList<>();
    private final List<Sportsman> bufferList = new ArrayList<>();

    public boolean addSportsman(Sportsman sportsman) {
        if(sportsmanList.isEmpty()) {
            sportsmanList.add(sportsman);
            return true;
        }
        if(!sportsman.command().equals(sportsmanList.getLast().command())) {
            sportsmanList.add(sportsman);
            if (bufferList.isEmpty()) {
                return true;
            } else {
                Sportsman last = bufferList.getLast();
                bufferList.removeLast();
                return addSportsman(last);
            }
        } else {
            bufferList.add(sportsman);
            return false;
        }
    }

    private boolean isValidShuffle(int index1, int index2) {
        Sportsman temp = sportsmanList.get(index1);
        sportsmanList.set(index1, sportsmanList.get(index2));
        sportsmanList.set(index2, temp);
        for(int i=0; i < sportsmanList.size()-1; i++) {
            if(sportsmanList.get(i).command().equals(sportsmanList.get(i+1).command())) {
                return false;
            }
        }
        return true;
    }

    public void shuffle() {
        Random random = new Random();
        for(int i=0; i < sportsmanList.size();i++) {
            int index1 = random.nextInt(sportsmanList.size());
            int index2 = random.nextInt(sportsmanList.size());
            if(index2 == index1) continue;
            Sportsman sportsman1 = sportsmanList.get(index1);
            Sportsman sportsman2 = sportsmanList.get(index2);
            if(sportsman1.command().equals(sportsman2.command())) {
                sportsmanList.set(index2, sportsman1);
                sportsmanList.set(index1, sportsman2);
            }
            else {
                if(!isValidShuffle(index1, index2)) {
                    sportsmanList.set(index1, sportsman1);
                    sportsmanList.set(index2, sportsman2);
                }
            }
        }
    }

    public String getResult() {
        if(sportsmanList.isEmpty()) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for(Sportsman sportsman:sportsmanList) {
            stringBuilder.append(sportsman.name()).append(',').append(sportsman.command()).append('\n');
        }
        return stringBuilder.toString();
    }



}


public class Main {
    private static SportsmanContainer parseSportsmenList(String filepath) {
        SportsmanContainer sportsmanContainer = new SportsmanContainer();
        try(BufferedReader reader = new BufferedReader( new FileReader(filepath))) {
            String line;
            while( ( line = reader.readLine() ) != null ) {
                String[] splitLine = line.split(",");
                sportsmanContainer.addSportsman(new Sportsman(splitLine[0], splitLine[1]));
            }
            return sportsmanContainer;
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл: "+filepath);
            return null;
        }
    }



    public static void main(String[] args) {
        String filepath = "src/sportsmen_list.txt";
        if(args.length > 1) {
            filepath = args[1];
        }
        SportsmanContainer sportsmanContainer = parseSportsmenList(filepath);
        if(sportsmanContainer == null) {
            return;
        }
        System.out.println(sportsmanContainer.getResult());
        sportsmanContainer.shuffle();
        System.out.println(sportsmanContainer.getResult());
    }
}