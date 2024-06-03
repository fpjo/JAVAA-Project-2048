package project.javaa.project2048.module;

import project.javaa.project2048.Direction;
import project.javaa.project2048.Location;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberTable {
    final int size;
    private final int MaxValue;
    private List<Location> locations;
    private int[][] table;

    static Random random = new Random();
    public NumberTable(){
        this(4, 2048);
    }
    public NumberTable(int size, int MaxValue) {
        this.size = size;
        table = new int[size][size];
        this.MaxValue = MaxValue;
    }
    private Optional<Location> findRandomAvailableLocation() {
        var availableLocations = locations.stream().filter(l -> table[l.y()][l.x()] == 0).collect(Collectors.toList());

        if (availableLocations.isEmpty()) {
            return Optional.empty();
        }
        Collections.shuffle(availableLocations);
        return Optional.of(availableLocations.get(new Random().nextInt(availableLocations.size())));
    }
    private void addRandomValue(Location location, int value) {
        if(value==0)value=random.nextDouble() < 0.8 ? 2 : 4;
        table[location.y()][location.x()] = value;
    }
    public void init() {
        for (int i = 0; i < table.length; i++) {
            Arrays.fill(table[i], 0);
        }
    }

    public int getValue(int x, int y) {
        return table[y][x];
    }

    public void setValue(int x, int y, int value) {
        table[y][x] = value;
    }

    public void clear() {
        for (int y = 0; y < table.length; y++) {
            for (int x = 0; x < table[y].length; x++) {
                table[y][x] = 0;
            }
        }
    }

    public boolean isFull() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (table[y][x] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    public int merge(Direction direction){
        int scores = 0;
        int xp0=0,yp0=0,xa=1,ya=1;
        if(direction==Direction.RIGHT){xp0=size-1;xa=-1;}
        if(direction==Direction.DOWN){yp0=size-1;ya=-1;}
        for(int yp=yp0;yp<size&&yp>=0;yp+=ya){
            for(int xp=xp0;xp<size&&xp>=0;xp+=xa){
                if(table[yp][xp]==0)continue;
                int x=xp,y=yp;
                int xm=direction.getX(),ym=direction.getY();
                while(x+xm>=0&&x+xm<size&&y+ym>=0&&y+ym<size&&table[y+ym][x+xm]==0){
                    table[y+ym][x+xm]=table[y][x];
                    table[y][x]=0;
                    x+=xm;
                    y+=ym;
                }
                if(x+xm>=0&&x+xm<size&&y+ym>=0&&y+ym<size&&table[y+ym][x+xm]==table[y][x]){
                    table[y+ym][x+xm]*=2;
                    scores+=table[y+ym][x+xm];
                    table[y][x]=0;
                }
            }
        }
        return 0;
    }
}
