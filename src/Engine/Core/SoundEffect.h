#ifndef SOUNDEFFECT_H_INCLUDED
#define SOUNDEFFECT_H_INCLUDED

//#include <SFML/audio.hpp>
//
//class AudioFile implements IReadWrite{
//    protected byte[] data;
//    protected Clip clip;
//
//    AudioFile(){}
//
//    AudioFile(File file) throws IOException{
//        FileInputStream fis = new FileInputStream(file);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//
//         byte[] buffer = new byte[4096];
//         int bytesRead;
//         while ((bytesRead = fis.read(buffer)) != -1)
//             os.write(buffer, 0, bytesRead);
//
//        data = os.toByteArray();
//    }
//
//    public void play() throws Exception{
//        if (clip == null) clip = AudioSystem.getClip();
//        ByteArrayInputStream dis = new ByteArrayInputStream(data);
//        AudioInputStream is = AudioSystem.getAudioInputStream(dis);
//        clip.open(is);
//        clip.start();
//    }
//
//    public void writeData(DataOutputStream dos) throws IOException {
//        dos.writeInt(data.length);
//        dos.write(data);
//    }
//
//    public void readData(DataInputStream dis) throws IOException {
//        int size = dis.readInt();
//        data = new byte[size];
//        dis.read(data);
//    }
//
//};




#endif // SOUNDEFFECT_H_INCLUDED
