package pallet;

import file.AFileResolver;
import game.*;
import image.ImageUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import util.UBTriple;

/**
 *
 * @author Gil Costa
 */
public class Pallet extends AFileResolver{
    //--------------
    // -- FIELDS --
    //--------------
    public HashMap<Color, Color> colors;
    
    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    public Pallet(String name){
        this();
        super.setFileName(GameInvariants.PALLETS_DIR + name + GameInvariants.PALLET_EXTENSION);
    }
    
    public Pallet(DataInputStream dis) throws IOException {
        this();
        this.readData(dis);
    }
    public Pallet(){
        super(GameInvariants.PALLET_EXTENSION, GameInvariants.PALLET_VERSION);
        colors = new HashMap<Color, Color>();
    }
    

    //---------------
    // -- METHODS --
    //---------------
    
    
    
     public BufferedImage applyToImage(BufferedImage img){
        BufferedImage target = ImageUtils.copyImage(img);
        for(int x=0; x<img.getWidth(); x++)
            for(int y=0; y<img.getHeight(); y++){
                // ignore transparent pixels
                if ((img.getRGB(x, y)&0xff000000) == 0)
                    continue;
                Color c = new Color(img.getRGB(x, y));
                if (c.getAlpha() == 0) continue;
                c = colors.get(c);
                if (c!=null){
                    int newRGB = c.getRGB();
                    target.setRGB(x, y, newRGB);
                }
            }
        return target;
    }
    
    
    
    
    
    
    
    protected void writeV1(DataOutputStream dos) throws IOException {
        
        dos.writeShort(colors.size());
        for(Map.Entry<Color, Color> entry:colors.entrySet()){
            Color c = entry.getKey();
            UBTriple key, value;
            key = new UBTriple(c.getRed(),c.getGreen(),c.getBlue());
            c = entry.getValue();
            value = new UBTriple(c.getRed(),c.getGreen(),c.getBlue());
            
            key.writeData(dos);
            value.writeData(dos);
        }
    }

    protected void readV1(DataInputStream dis) throws IOException {
        // v1.1
//        int size = dis.read();
//        char[] txt= new char[size];
//         for(int i=0; i<size; i++){
//            //txt[i] = dis.readChar();  // v1.0
//            txt[i] = (char)dis.readByte();  // v1.1
//         }
        
            
        int size = dis.readShort();
        colors = new HashMap<Color, Color>(size);
        for(int i=0; i<size; i++){
            UBTriple key = new UBTriple(dis);
            UBTriple value = new UBTriple(dis);
            colors.put(new Color(key.getX(),key.getY(),key.getZ()), new Color(value.getX(),value.getY(),value.getZ()));
        }
    }
    
    
    
    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        switch(versionID){
            case 1: writeV1(dos); break;
            default: writeV1(dos);
        }
    }
     
    @Override
    public void readData(DataInputStream dis) throws IOException {
        switch(versionID){
            case 1: readV1(dis); break;
            default: readV1(dis);
        }
    }
    
    
}
