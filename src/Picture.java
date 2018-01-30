import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

public class Picture {

public static boolean farenought(boolean a, boolean b, boolean c)
{
								return((a&&b)||((a^b)&&c));
}

public static boolean goodcolor(int i, int j, int k, int red, int green, int blue, int nbcol, double close)
{
								return (farenought(((double)(Math.abs(i-red))/(double)nbcol>=close),
																											((double)(Math.abs(j-green))/(double)nbcol>=close),
																											((double)(Math.abs(k-blue))/(double)nbcol>=close)));
}

public static boolean goodcolor2(int i, int j, int k, int red, int green, int blue, int nbcol, double close)
{
								return (Math.sqrt((red-i)*(red-i)+(green-j)*(green-j)+(blue-k)*(blue-k))>=close*Math.sqrt(nbcol*nbcol));
}

public static void  getColor(BufferedImage file)
{

								// Getting pixel color by position x and y
								int height = file.getHeight();
								int width = file.getWidth();
								HashMap<int[], Integer> countercolors = new HashMap<int[], Integer>();
								int color,red,green,blue;
								int[] rgb = new int[3];


								for(int i = 0; i < height; i++) {
																for(int j = 0; j < width; j++) {
																								color =  file.getRGB(j,i);
																								red   = ((color & 0x00ff0000) >> 16) /100;
																								green = ((color & 0x0000ff00) >> 8) /100;
																								blue  =  (color & 0x000000ff)/100;
																								rgb = new int[] {100*red,100*green,100*blue};

																								if(countercolors.containsKey(rgb))
																																countercolors.put(rgb, countercolors.get(rgb)+1);
																								else
																																countercolors.put(rgb, 1);

																}
								}
								System.out.println(countercolors.size());
}

public static Color  getColor2(BufferedImage file, int precision)
{
								int nbcol = (255*precision)/100+1;
								System.out.println(nbcol);
								int height = file.getHeight();
								int width = file.getWidth();
								int[][][] countercolors = new int[nbcol][nbcol][nbcol];
								int color,red,green,blue;

								for(int i = 0; i < nbcol; i++)
																for(int j = 0; j < nbcol; j++)
																								for(int k = 0; k < nbcol; k++)
																																countercolors[i][j][k]=0;

								for(int i = 0; i < height; i++) {
																for(int j = 0; j < width; j++) {
																								color =  file.getRGB(j,i);
																								red   = ((color & 0x00ff0000) >> 16)*precision /100;
																								green = ((color & 0x0000ff00) >> 8)*precision /100;
																								blue  =  (color & 0x000000ff)*precision /100;
																								if(red<nbcol && green <nbcol && blue<nbcol)
																																countercolors[red][green][blue]++;
																								else
																																System.out.println(red*100 /precision+" "+green*100 /precision+" "+blue*100 /precision);

																								// System.out.println((float)red*100 /(float)precision+" "+green*100 /precision+" "+blue*100 /precision);

																}
								}

								int max = 0;
								red=-1;
								green=-1;
								blue=-1;

								// System.out.println(height+" "+width);

								for(int i = 0; i < nbcol; i++)
								{
																// System.out.println(max);
																for(int j = 0; j < nbcol; j++)
																{
																								for(int k = 0; k < nbcol; k++)
																								{
																																if (countercolors[i][j][k]>max)
																																{
																																								max=countercolors[i][j][k];
																																								red=i;
																																								green=j;
																																								blue=k;
																																}
																								}
																}
								}

								// System.out.println(max+" "+red*100 /precision+" "+green*100 /precision+" "+blue*100 /precision);



								int cred=-1;
								int cgreen=-1;
								int cblue=-1;
								double close=0.2; //0.15
								max =0;
								for(int i = 0; i < nbcol; i++)
																for(int j = 0; j < nbcol; j++)
																								for(int k = 0; k < nbcol; k++)
																																if (goodcolor2(i, j, k, cred, cgreen, cblue, nbcol, close) && countercolors[i][j][k]>max)
																																{
																																								max=countercolors[i][j][k];
																																								cred=i;
																																								cgreen=j;
																																								cblue=k;
																																}

								int ired = 255-red*100 /precision;
								int igreen = 255-green*100 /precision;
								int iblue = 255-blue*100 /precision;

								System.out.println("Couleur la plus presente :"+red*100 /precision+" "+green*100 /precision+" "+blue*100 /precision);
								System.out.println("Couleur PROCHE la plus presente :"+cred*100 /precision+" "+cgreen*100 /precision+" "+cblue*100 /precision);
								System.out.println("Couleur OPPOSEE a la plus presente :"+ired+" "+igreen+" "+iblue);

								Color c= new Color((float) cred*100 /precision/255, (float)cgreen*100 /precision/255, (float)cblue*100 /precision/255);
								return c;

}

public static void randompic(BufferedImage im)
{
								int h=im.getHeight();
								int w=im.getWidth();
								int r,g,b,a, col;
								for(int i=0; i<h; i++)
								{
																for(int j=0; j<w; j++)
																{
																								r = ThreadLocalRandom.current().nextInt(0, 256); // red component 0...255
																								g = ThreadLocalRandom.current().nextInt(0, 256);// green component 0...255
																								b = ThreadLocalRandom.current().nextInt(0, 256);// blue component 0...255
																								a = 255; // alpha (transparency) component 0...255
																								col = (a << 24) | (r << 16) | (g << 8) | b;
																								// System.out.println(col);
																								im.setRGB(j, i, col);
																}
								}
}

public static void pixelise(BufferedImage im, int[] col, int n, int m)
{
								int h=im.getHeight();
								int w=im.getWidth();
								int c;
								int hb =h/n;
								int wb =w/m;
							//	int cb= (255 << 24) | (0 << 16) | (255 << 8) | 0;
								int t=0;
								for(int i=0; i<=n; i++)
								{
																for(int j=0; j<=m; j++)
																{
																								t=j*m+i;
																								c=col[t];
																								for(int k=i*hb; k<(i+1)*hb; k++)
																								{
																																for(int l=j*wb; l<(j+1)*wb; l++)
																																{
																																								if(l<w && k<h)
																																																im.setRGB(l,k,c);
																																}
																								}
																}
								}


}

public static int[] getcolors(BufferedImage im, int n, int m)
{
								int[] col=new int[(n+1)*(m+1)];
								int h=im.getHeight();
								int w=im.getWidth();
								int c,r,g,b;
								int hb =h/n;
								int wb =w/m;
								// System.out.println("hauteur bloc ="+hb+"\nlargeur bloc="+wb);

								for(int i=0; i<=n; i++)
								{
																for(int j=0; j<=m; j++)
																{
																								r=g=b=0;
																								for(int k=i*hb; k<(i+1)*hb; k++)
																								{
																																for(int l=j*wb; l<(j+1)*wb; l++)
																																{
																																								if(l<w && k<h)
																																								{
																																																c=  im.getRGB(l,k);
																																																r+= ((c & 0x00ff0000) >> 16);
																																																g+= ((c & 0x0000ff00) >> 8);
																																																b+=  (c & 0x000000ff);
																																								}
																																}
																								}
																								r=r/(hb*wb);
																								g=g/(hb*wb);
																								b=b/(hb*wb);

																								col[j*m+i]=(255 << 24) | (r << 16) | (g << 8) | b;
																}
								}

								return col;

}

public static boolean isPositiveInteger(String s, int radix) {
								if(s.isEmpty()) return false;
								for(int i = 0; i < s.length(); i++) {
																if(Character.digit(s.charAt(i),radix) < 0) return false;
								}
								return true;
}

public static void main(String[] args) throws IOException {

								String myfile, newfile;// = myfilename+'.'+myfileextension;
								int param1, param2;
								BufferedImage image, off_Image;
								File f;

								if(args.length != 0) {
																if(args[0].length()<2 || args[0].charAt(0)!='-')
																{
																								throw new IllegalArgumentException("Not a valid argument: "+args[0]);
																}
																char opt = args[0].charAt(1);
																switch (opt)
																{

																case 'p':

																								if((args.length != 3 && args.length !=4) || !isPositiveInteger(args[1],10))
																																throw new IllegalArgumentException("Arguments of "+args[0]+" not valid.\nExpected -p number inputfile outputfile(optional)");

																								myfile = args[2];
																								param1 = Integer.parseInt(args[1]);
																								param2=param1;
																								File file= new File(myfile);
																								image = ImageIO.read(file);

																								off_Image =
																																new BufferedImage(image.getWidth(), image.getHeight(),
																																																		BufferedImage.TYPE_INT_ARGB);
																								int[] col=getcolors(image, param1, param2);
																								pixelise(off_Image, col,  param1, param2);
																								String[] parts = myfile.split("\\.");
																								String name = parts[0];
																								String extension = parts[1];

																								if(args.length==3)
																																newfile = name+"_pixelised("+param1+")."+extension;
																								else
																																newfile = args[3];

																								f = new File(newfile);
																								ImageIO.write(off_Image, "png", f);
																								System.out.println("Image "+newfile+" created!");
																								break;

																case 'r':

																								if((args.length < 3) || !isPositiveInteger(args[1],10) || !isPositiveInteger(args[2],10) ) // || ((args.length == 2 || args.length == 3 && !isPositiveInteger(args[1],10))
																																throw new IllegalArgumentException("Arguments of "+args[0]+" not valid.\nExpected -r width height number(optional)[TODO] outputfile(optional)");

																								param1 = Integer.parseInt(args[1]);
																								param2 = Integer.parseInt(args[2]);

																								off_Image = new BufferedImage(param1, param2, BufferedImage.TYPE_INT_ARGB);
																								randompic(off_Image);

																								if(args.length==4)
																																newfile = args[3];
																								else
																																newfile = "random_pic("+param1+","+param2+").png";


																								f = new File(newfile);
																								ImageIO.write(off_Image, "png", f);
																								System.out.println("Image "+newfile+" created!");
																								break;


																default:
																								System.out.println("Unrecongnized option. Available options are:\n\t-p number inputfile outputfile(optional)\n\t-r width height number(optional)[TODO] outputfile(optional)");

																								break;
																}
								}


/*



        String myfilename = "pic4";
        String myfileextension ="jpg";
        String myfile = myfilename+'.'+myfileextension;

        File file= new File(myfile);
        BufferedImage image = ImageIO.read(file);
        BufferedImage off_Image =
                new BufferedImage(image.getWidth(), image.getHeight(),
                                  BufferedImage.TYPE_INT_ARGB);

        int n=50;
        int m=50;
        int[] col=getcolors(image, n, m);
        Color c = getColor2(image, 60);
        //randompic(off_Image);
        pixelise(off_Image, col, n, m);
        File f = new File(myfilename+"_pixelised("+n+","+m+')');
        ImageIO.write(off_Image, "png", f);


 */
}
}
