package characters;

import weapons.*;
import frames.*;
import file.ACollection;
import file.AFileResolver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import util.UByte;



/**
 * Images collection
 * @author Gil Costa
 * 
 * File Structure:
 * N				2 bytes		-> number of frames
 * I				1 byte		-> if cm are present for each frame
 *  cmx1...cmxN		N bytes		-> CM x coordinates
 *  cmy1...cmyN		N bytes		-> CM y coordinates
 *  size1...sizeN	N*4 bytes	-> size of each frame
 *  img1...imgN		4*(size1+...+sizeN) bytes	-> frames data    
 *
 */
public class Character extends AFileResolver{ 
    public HashMap<Integer,Animation> animSet;
    
    public ArrayList<String> names;
    public ArrayList<String> anims;
    public ArrayList<String> heads;
    public ArrayList<String> icons;
    
    public ArrayList<String> replacements;  // string containing 3 integers
    
    
    public UByte walkVel;
    public UByte jumpVel;
    public UByte runJumpVel;
    public boolean autoPallete;
    
    
	/** Constructor: an empty FramesCollection */
	public Character(){
        super(game.GameInvariants.CHARACTER_EXTENSION, game.GameInvariants.CHARACTER_VERSION);
        names = new ArrayList<String>();
        anims = new ArrayList<String>();
        heads = new ArrayList<String>();
        icons = new ArrayList<String>();
        replacements = new ArrayList<String>();
        walkVel = new UByte();
        jumpVel = new UByte();
        runJumpVel = new UByte();
        animSet = new HashMap<Integer,Animation>();
	}	
	
    
   
    
    
    public void animsControll(){
        if (animSet == null) return;
        for(Animation e:animSet.values()){
            e.headSwap = hasHeadSwaps();
        }
    }
    
    
    public HashMap<Integer,Animation> getAnimsSet(){
        animsControll();
        return animSet;
    }
    
    
    
    public void addAnimation(String name){
        anims.add(name);
    }
    public void addHeadSwap(String name){
        heads.add(name);
    }
    public void addIcon(String name){
        icons.add(name);
    }
    public void addName(String name){
        names.add(name);
    }
    public void addReplacement(String name){
        replacements.add(name);
    }
    public void setName(int index, String name){
        names.set(index, name);
    }
    public void setAnimName(int index, String name){
        anims.set(index, name);
    }
    public void setHeadName(int index, String name){
        heads.set(index, name);
    }
    public void setIconName(int index, String name){
        icons.set(index, name);
    }
    public void setReplacement(int index, String name){
        replacements.set(index, name);
    }
    
    public boolean animExists(String anim){
        return anims.contains(anim);
    }
    public boolean headExists(String head){
        return heads.contains(head);
    }
    public boolean iconExists(String icon){
        return icons.contains(icon);
    }
    public boolean nameExists(String name){
        return names.contains(name);
    }
    public boolean replacementExists(String repl){
        return replacements.contains(repl);
    }
    
    public String getFramesCollection(int i){
        if (i<0 || i>=anims.size()) return null;
        return anims.get(i);
    }
    public Animation getAnim(int i){
        animsControll();
        return animSet.get(i);
    }
    public String getAnimName(int i){
        if (i<0 || i>=anims.size()) return null;
        return anims.get(i);
    }
    public String getHeadName(int i){
        if (i<0 || i>=heads.size()) return null;
        return heads.get(i);
    }
    public String getIconName(int i){
        if (i<0 || i>=icons.size()) return null;
        return icons.get(i);
    }
    public String getReplacement(int i){
        if (i<0 || i>=replacements.size()) return null;
        return replacements.get(i);
    }
    public String getName(int i){
        if (i<0 || i>=names.size()) return null;
        return names.get(i);
    }
    
    public void removeAnim(int i){
        if (i<0 || i>=anims.size()) return;
        anims.remove(i);
    }
    public void removeHead(int i){
        if (i<0 || i>=heads.size()) return;
        heads.remove(i);
    }
    public void removeIcon(int i){
        if (i<0 || i>=icons.size()) return;
        icons.remove(i);
    }
    public void removeName(int i){
        if (i<0 || i>=names.size()) return;
        names.remove(i);
    }
    public void removeReplacement(int i){
        if (i<0 || i>=replacements.size()) return;
        replacements.remove(i);
    }
	
    public boolean hasHeadSwaps(){
        return heads!= null && !heads.isEmpty();
    }
    public boolean hasIcons(){
        return icons!= null && !icons.isEmpty();
    }
    
    
    
    
	//-----------------------------
	// ------- LOAD / SAVE -------
	
    
    
    public Animation readElement(DataInputStream dis) throws IOException {
        Animation anim = new Animation(versionID);
        anim.headSwap = hasHeadSwaps();
        anim.readData(dis);
        return anim;
    }
    
    
    protected void headsControl(){
        for(Animation anim:animSet.values())
            anim.headSwap = hasHeadSwaps();
    }
    

    protected void replacementsControl(){
        ArrayList<Integer> removeLst = new ArrayList<Integer>();
        for(int i= 0; i< replacements.size(); i++){
            Scanner sc = new Scanner(replacements.get(i));
            int ori = sc.nextInt();    // original
            int dest = sc.nextInt();    // replacement
            // ignore weapon
            if (!animSet.containsKey(ori) || !animSet.containsKey(ori))
                removeLst.add(i);
        }
        for(int i:removeLst)
            replacements.remove(i);
    }
    
    
    @Override
    public void setVersion(int versionID){
        this.versionID = versionID;
        for(Animation g:animSet.values()){
            g.setVersion(versionID);
        }
    }
    

    public void writeHeader(DataOutputStream dos) throws IOException {
        headsControl();
        
        
         // write names
        int n = names.size();
        dos.write(n);
        for(String s:names){
            dos.write(s.length());
            dos.writeBytes(s);
        }
        
        // write animation collections names (v 1.2 swaped with heads)
        n = anims.size();
        dos.write(n);
        for(String s:anims){
            dos.write(s.length());
            dos.writeBytes(s);
        }
        
        // write heads
        n = heads.size();
        dos.write(n);
        for(String s:heads){
            dos.write(s.length());
            dos.writeBytes(s);
        }
        
        // write icons (v1.2)
        n = icons.size();
        dos.write(n);
        for(String s:icons){
            dos.write(s.length());
            dos.writeBytes(s);
        }
        
        
        // write options
        walkVel.writeData(dos);
        jumpVel.writeData(dos);
        runJumpVel.writeData(dos);
        dos.writeBoolean(autoPallete);
        
    }

    
    public void readHeader(DataInputStream dis)  throws IOException {
        
        // read names
        int n = dis.read();
        names = new ArrayList<String>(n);
        for(int i=0; i<n; i++){
            int size = dis.read();
            char[] txt= new char[size];
             for(int j=0; j<size; j++){
                //txt[j] = dis.readChar();      // v1.0
                 txt[j] = (char)dis.readByte(); // v1.1
             }
            names.add(new String(txt));
        }
        
        // read animation collections names (v1.2 swaped with heads reading)
        n = dis.read();
        anims = new ArrayList<String>(n);
        for(int i=0; i<n; i++){
            int size = dis.read();
            char[] txt= new char[size];
            for(int j=0; j<size; j++){
                //txt[j] = dis.readChar();      // v1.0
                 txt[j] = (char)dis.readByte(); // v1.1
             }
            anims.add(new String(txt));
        }
        
        // read heads
        n = dis.read();
        heads = new ArrayList<String>(n);
        for(int i=0; i<n; i++){
            int size = dis.read();
            char[] txt= new char[size];
             for(int j=0; j<size; j++){
                //txt[j] = dis.readChar();      // v1.0
                 txt[j] = (char)dis.readByte(); // v1.1
             }
            heads.add(new String(txt));
        }
        
        // read icons (v1.2)
        n = dis.read();
        icons = new ArrayList<String>(n);
        for(int i=0; i<n; i++){
            int size = dis.read();
            char[] txt= new char[size];
             for(int j=0; j<size; j++){
                 txt[j] = (char)dis.readByte();
             }
            icons.add(new String(txt));
        }
        
        // read options (v1.2)
        walkVel = new UByte(dis);
        jumpVel = new UByte(dis);
        runJumpVel = new UByte(dis);
        autoPallete = dis.readBoolean();
        
    }
    
    
    
    
    public void writeFoother(DataOutputStream dos)  throws IOException{
        // write replacements (v1.2)
        // remove invalid replacements
        replacementsControl();
        int n = replacements.size();
        dos.write(n);
        for(String s:replacements){
            Scanner sc = new Scanner(s);
            dos.write(sc.nextInt());    // original
            dos.write(sc.nextInt());    // replacement
            dos.write(sc.nextInt());    // weapon
        }
    }
    
    
    public void readFoother(DataInputStream dis)  throws IOException{
        // read replacements (v1.2)
        int n = dis.read();
        replacements = new ArrayList<String>(n);
        for(int i=0; i<n; i++){
            String s = dis.read() + "";
            s+="  "; s+=dis.read();
            s+="  "; s+=dis.read();
            replacements.add(s);
        }
    }

    
    
    
protected void writeV1(DataOutputStream dos) throws IOException{        
        writeHeader(dos);
        
		int N = animSet.size();
		// write number of animations
		dos.writeShort((short)N);
		
        // write all animations
        for(Animation g:animSet.values()){
            g.writeData(dos);
        }
        
        writeFoother(dos);
	}
	
	
    
	protected void readV1(DataInputStream dis) throws IOException{
        readHeader(dis);
        
		// read number of animations
		int N = dis.readShort();
		animSet = new HashMap<Integer,Animation>(N);
        // read all animations
        for(int i=0; i< N; i++){
            Animation a = readElement(dis);
            animSet.put(a.index.get(), a);
        }
        
        readFoother(dis);
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
