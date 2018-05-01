import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by User on 05/06/2017.
 */
public class Server
{
    static boolean isFirst = true;
    static String player;
    static String sent;
    static int[][] xo = new int[3][3];
    private static boolean isX = true;
    private static boolean haswon = false;
    static Integer ltr;
    static Integer ltc;
    static String lastplayer;

    public static void main(String[] args) {
        try {//*
            ServerSocket server = new ServerSocket(999);
//            server.setSoTimeout(2*60*1000);//10 seconds
            while(true) {
                Socket client=server.accept();//get connected client socket
                System.out.println(client.getInetAddress());//remote address
                InputStream is = client.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                //read data from client: byte by byte
                String req = dis.readUTF();
                System.out.println(req);
                if(req.equals("whoami"))
                {
                    sent = orderPlayers();
                }
                else if(req.equals("refresh"))
                {
                    sent = refresh();
                }
                else
                {
                    sent = makeMove(req);
                }
                OutputStream os = client.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF(sent);
                client.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String orderPlayers()
    {
        player = isFirst ? "X" : "O";
        isFirst = false;
        return player;
    }

    public static String refresh()
    {
        String currplayer = isX ? "X" : "O";
        if(ltr != null) {
            return currplayer + "," + lastplayer + "," + ltr + "," + ltc;
        }
        else
        {
            return "n";
        }
    }

    public static String makeMove(String request)
    {
        String[] alls = request.split(",");
        int row = Integer.parseInt(alls[0]);
        ltr = row;
        int col = Integer.parseInt(alls[1]);
        ltc = col;
        boolean player = Boolean.parseBoolean(alls[2]);
        lastplayer = player ? "X" : "O";
        xo[row][col] = isX ? 1 : 2;
        if (hasWon(row, col))
        {
            haswon = true;
            return "gameover";
        }
        else
        {
            player = !player;
            isX = player;
            return "nope";
        }
    }

    private static boolean hasWon(int row, int col){
        int i=0,matches=0;
        final int dist = Math.abs(row-col);//
        final int last = xo.length-1;
        final int current = xo[row][col];
        if(dist == last || row == col ) {
            //diagonal
            for (i = 0, matches = 0; i <= last; i++) {
                if (current == xo[i][i]) {
                    matches++;
                    if (matches == xo.length)
                        return true;
                } else {
                    break;
                }
            }
            //rev diagonal
            for(i=0,matches=0;i<=last;i++) {
                if (current == xo[i][last-i]) {
                    matches++;
                    if (matches == xo.length)
                        return true;
                } else {
                    break;
                }
            }
        }

        // rows
        for(i=0,matches=0;i<=last;i++) {
            if (current == xo[i][col]) {
                matches++;
                if (matches == xo.length)
                    return true;
            } else {
                break;
            }
        }
        //cols
        for(i=0,matches=0;i<=last;i++) {
            if (current == xo[row][i]) {
                matches++;
                if (matches == xo.length)
                    return true;
            } else {
                break;
            }
        }
        return false;
    }
}
