package br.com.giorgetti.games.squareplatform.gameobjects.interaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgiorgetti on 20/07/17.
 */
public class NameDataInput extends DataInput {
    @Override
    public List<String> parameterList() {
        List<String> list = new ArrayList<>();
        list.add("Name");
        list.add("Age");
        list.add("Birth");
        return list;
    }

    @Override
    public void complete() {

        for ( String param : parameterList() ) {
            System.out.println(param + "=> " + getDataInput(param));
        }
    }
}
