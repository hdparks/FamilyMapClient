package com.example.familymapclient.model;

import com.example.familymapclient.DataCache;

import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

public class FamilyTree {

    public Map<String, FamilyTreeNode> nodeMap;

    private class FamilyTreeNode {
        public Person person;
        public FamilyTreeNode father;
        public FamilyTreeNode mother;
        public FamilyTreeNode spouse;
        public List<FamilyTreeNode> children;

    }

    FamilyTree(DataCache dataCache){
        dataCache.
    }



}
