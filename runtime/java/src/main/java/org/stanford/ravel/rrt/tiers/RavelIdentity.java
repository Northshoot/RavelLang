package org.stanford.ravel.rrt.tiers;


import org.stanford.ravel.rrt.utils.ByteWork;

import java.io.*;
import java.nio.ByteBuffer;

public class RavelIdentity {

    public int tier;
    public int id;
    public final static int length = 8;

    private RavelIdentity(int t, int id){
        this.tier = t;
        this.id = id;
    }

    private RavelIdentity(int t){
        this.tier = t;
    }

    public static RavelIdentity identityFromBytes(byte[] data) throws IOException, ClassNotFoundException {

        int tier = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, 4,8));
        int id = ByteWork.convertFourBytesToInt(ByteWork.getBytes(data, 0,4));
        System.out.println("From stream: tier " + tier + " ID " + id);
        return RavelIdentity.makeLocalIdentity(tier, id);
    }

    public static byte[] identityToBytes(RavelIdentity ri) throws IOException {
        ByteBuffer bf = ByteBuffer.allocate(RavelIdentity.length);
        bf.putInt(ri.tier);
        bf.putInt(ri.id);
        return bf.array();

    }

    public static void setId(RavelIdentity ri, int id){
        ri.id = id;
    }

    public static RavelIdentity makeLocalIdentity(int tier, int id){
        return new RavelIdentity(tier, id);
    }

    public static RavelIdentity makeRemoteIdentity(int tier){
        return new RavelIdentity(tier);
    }
    public String toString(){
        return "IDNT[tier: " + this.tier + ", ID: " + this.id + "]";
    }

}