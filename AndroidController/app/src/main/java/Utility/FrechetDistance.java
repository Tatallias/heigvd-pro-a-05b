package Utility;





/**
 * class used to compute the frechet distance between two discrete paths
 * implementing the algorithm described in this paper http://www.kr.tuwien.ac.at/staff/eiter/et-archive/cdtr9464.pdf
 */
public class FrechetDistance {
    float [][] ca;
    Vector2[] P;
    Vector2[] Q;

    public float distance(Vector2[] P, Vector2[] Q){
       ca= new float [P.length][Q.length];
       this.P=P;
       this.Q=Q;
        for (int i = 0; i < P.length ; i++) {
            for (int j = 0; j < Q.length ; j++) {
                ca[i][j]= -1;
            }
        }

        return internalDistance(P.length-1,Q.length-1);

    }

    private float internalDistance(int i, int j){
        if(ca[i][j]> -1){
            return ca[i][j];
        }else if (i==0&& j==0){
            ca[i][j]=P[i].dist2(Q[i]);
        }else if (i>0 && j==0){
            ca[i][j]=Math.max(internalDistance(i-1,j),P[i].dist2(Q[i]));
        }else if (i==0 && j>0) {
            ca[i][j]=Math.max(internalDistance(i,j-1),P[i].dist2(Q[i]));
        }else if(i>0&&j>0){

            ca[i][j]=Math.max(Math.min(Math.min(internalDistance(i,j-1),internalDistance(i-1,j)),internalDistance(i-1,j-1)),P[i].dist2(Q[i]));
        }else{
            ca[i][j]=Float.MAX_VALUE;
        }
        return ca[i][j];
    }
}
