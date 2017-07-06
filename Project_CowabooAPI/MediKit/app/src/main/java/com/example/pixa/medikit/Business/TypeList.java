package com.example.pixa.medikit.Business;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joel Marques on 17.05.2017.
 */

public class TypeList {
    private Collection<Type> types = new ArrayList<Type>();
    public void addType(Type s) {types.add(s);}
    public Collection<Type> getTypes() {return types;}
}
