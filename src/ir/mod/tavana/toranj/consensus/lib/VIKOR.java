/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import sun.misc.Compare;

/**
 *
 * @author Admin
 */
public class VIKOR {
    private Matrix[] decisionMatrixes;
    private Matrix[] criteriaWeightMatrixes;
    private int numberOfDecisionMakers;
    private boolean echo = false;
    private float veto;
    public LinkedList trace()
    {
        //step 1: summarizing all opinions
        numberOfDecisionMakers = decisionMatrixes.length;
        Matrix decisionMatrix , criteriaWeightingMatrix;
        decisionMatrix = decisionMatrixes[0];
        criteriaWeightingMatrix = criteriaWeightMatrixes[0];
        for(int i = 1 ; i < numberOfDecisionMakers ; i++)
        {
            decisionMatrix = decisionMatrix.sum(decisionMatrix, decisionMatrixes[i]);
            criteriaWeightingMatrix = criteriaWeightingMatrix.sum(criteriaWeightingMatrix, 
                    criteriaWeightMatrixes[i]);
        }
        decisionMatrix = decisionMatrix.divide(decisionMatrix, numberOfDecisionMakers);
        criteriaWeightingMatrix = criteriaWeightingMatrix.divide(criteriaWeightingMatrix,
                numberOfDecisionMakers);
        if(echo){
            System.out.println("============AX===========");
            System.out.println(decisionMatrix.toString());
            System.out.println("============AW===========");
            System.out.println(criteriaWeightingMatrix.toString());
        }
        Matrix X = decisionMatrix.multiply(decisionMatrix , criteriaWeightingMatrix);
        if(echo){
            System.out.println("============X============");
            System.out.println(X.toString());
        }

        //step 2: calculate f*[j] and f-[j]
        if(echo){
            System.out.println("Calculating F* and F-");
        }
        float [] fStar = new float[decisionMatrix.getLength()];
        float [] fMinus = new float[decisionMatrix.getLength()];
        for(int j = 0 ; j < decisionMatrix.getLength() ; j++)
        {
            float min = (Float)decisionMatrix.getElement(0, j);
            float max = (Float)decisionMatrix.getElement(0, j);
            for(int i = 0 ; i < decisionMatrix.getHeight() ; i++)
            {
                max = Math.max(max, (Float)decisionMatrix.getElement(i, j));
                min = Math.min(min, (Float)decisionMatrix.getElement(i, j));
            }

            fStar[j]= max; fMinus[j] = min;
            if(echo){System.out.println("f*["+j+"] = "+fStar[j]+"; f-["+j+"] = "+fMinus[j]);}
        }

        //step 3: calculate R and S parameters. Hold the amount of S*, S-, R* and R-
        float [] R = new float[decisionMatrix.getHeight()];
        float [] S = new float[decisionMatrix.getHeight()];
        float sStar = -1 , rStar = -1 , sMinus = -1 , rMinus = -1;
        for (int i = 0 ; i < decisionMatrix.getHeight() ; i++)
        {
            R[i] = (Float) decisionMatrix.getElement(i, 0);
            float sum = 0;
            for (int j = 0 ; j < decisionMatrix.getLength() ; j++)
            {
                if(fStar[j]!= fMinus[j]){
                    float tmp = Math.abs((Float)criteriaWeightingMatrix.getElement(j, 0)*
                        (fStar[j]-(Float)decisionMatrix.getElement(i, j))/(fStar[j]-fMinus[j]));
                    sum += tmp;
                    R[i]= Math.max(R[i], tmp);
                }else {sum = 0; R[i]=0;}
            }
            S[i] = sum;
            if(echo){
                System.out.println(">>S["+i+"]="+S[i]);
                System.out.println(">>R["+i+"]="+R[i]);
            }
            if(rMinus < 0) rMinus = R[i];
            else rMinus = Math.min(rMinus, R[i]);
            if(rStar < 0) rStar = R[i];
            else rStar = Math.max(rStar, R[i]);
            if(sMinus < 0) sMinus = S[i];
            else sMinus = Math.min(sMinus, S[i]);
            if(sStar < 0) sStar = S[i];
            else sStar = Math.max(sStar, S[i]);
        }
        if(echo){
            System.out.println();
            System.out.println(">S*="+sStar);
            System.out.println(">R*="+rStar);
            System.out.println(">S-="+sMinus);
            System.out.println(">R-="+rMinus);
        }
        

        //step 4: calculate Q
        float [] Q = new float[decisionMatrix.getHeight()];
        //setVeto((float) 0);
        if(echo){
            System.out.println();
        }
        for (int i = 0 ; i < decisionMatrix.getHeight() ; i++)
        {
            Q[i] = getVeto()*(S[i]-sStar)/(sMinus-sStar) + (1-getVeto())*(R[i]-rStar)/(rMinus-rStar);
            if(echo){System.out.println(">Q["+i+"]="+Q[i]);}
        }

        //step 5: sorting arrays on the basis of S, Q and R
        
        LinkedList<TempForSort> listS = new LinkedList();
        LinkedList<TempForSort> listR = new LinkedList();
        LinkedList<TempForSort> listQ = new LinkedList();
        for(int i = 0 ; i < S.length ; i++)
        {
            listS.add(new TempForSort(i,S[i]));
            listR.add(new TempForSort(i,R[i]));
            listQ.add(new TempForSort(i,Q[i]));
        }
        LinkedList<TempForSort> sortedListS = new LinkedList();
        LinkedList<TempForSort> sortedListR = new LinkedList();
        LinkedList<TempForSort> sortedListQ = new LinkedList();
        for(int i = 0 ; i < S.length ; i++)
        {
            int tmpS = 0 , tmpR = 0 , tmpQ = 0;
            for(int j = 0 ; j < listS.size() ; j++){
                if((Float)listS.get(j).value > (Float)listS.get(tmpS).value)
                    tmpS = j;
                if((Float)listR.get(j).value > (Float)listR.get(tmpR).value)
                    tmpR = j;
                if((Float)listQ.get(j).value > (Float)listQ.get(tmpQ).value)
                    tmpQ = j;
            }
            sortedListS.addLast(listS.get(tmpS));
            listS.remove(tmpS);
            sortedListR.addLast(listR.get(tmpR));
            listR.remove(tmpR);
            sortedListQ.addLast(listQ.get(tmpQ));
            listQ.remove(tmpQ);
        }
        if(echo){
            System.out.print("\n>>>Sort based on S parameter: ");
            for(int i = 0 ; i < sortedListS.size() ; i++)
            {
                System.out.print(sortedListS.get(i).key+"=>"+sortedListS.get(i).value+" , ");
            }
            System.out.print("\n>>>Sort based on R parameter: ");
            for(int i = 0 ; i < sortedListR.size() ; i++)
            {
                System.out.print(sortedListR.get(i).key+"=>"+sortedListR.get(i).value+" , ");
            }
            System.out.print("\n>>>Sort based on Q parameter: ");
            for(int i = 0 ; i < sortedListQ.size() ; i++)
            {
                System.out.print(sortedListQ.get(i).key+"=>"+sortedListQ.get(i).value+" , ");
            }
        }
        //step 6: Checking if the result meets two conditions C1 and C2
        float DQ;
        if(S.length < 2) DQ = 0;
        else DQ = 1/(S.length-1);
        TempForSort selectedAlternative = sortedListQ.pop();
        TempForSort nextSelectedAlternative = sortedListQ.pop();
        if(echo){
            System.out.println("The selected alternative is number "+selectedAlternative
                +" and the next one is number "+nextSelectedAlternative);
        }
        boolean C1;
        if((Float)nextSelectedAlternative.value-(Float)selectedAlternative.value >= DQ) C1 = true;
        else C1 = false;
        if(echo){
            if(C1) System.out.println("****C1 checked");
            else System.out.println("C1 is FALSE");
        }

        boolean C2;
        if(sortedListR.getLast().key == selectedAlternative.key ||
                sortedListS.getLast().key == selectedAlternative.key)
            C2 = true;
        else C2 = false;
        if(echo)
        {
            if (C2)
                System.out.println("****C2 checked");
            else System.out.println("C2 is FALSE");
        }

        LinkedList<TempForSort> bestAlternatives = new LinkedList();

        if(!C1)
        {
            bestAlternatives.add(selectedAlternative);
            bestAlternatives.add(nextSelectedAlternative);
            while((Float)nextSelectedAlternative.value-(Float)selectedAlternative.value < DQ
                    && sortedListQ.size()>0)
            {
                nextSelectedAlternative = sortedListQ.pop();
                bestAlternatives.add(nextSelectedAlternative);
            }
        } 
        if(C1 && !C2) {
            bestAlternatives.add(selectedAlternative);
            bestAlternatives.add(nextSelectedAlternative);
        }
        if(C1 && C2){
            bestAlternatives.clear();
            bestAlternatives.add(selectedAlternative);
        }
        
        if(echo){
            System.out.println();
            System.out.println("==================================");
            System.out.println(bestAlternatives.size()+" alternatives were chosen:");
        }
        LinkedList<Integer> indexesOfTheBestAlternatives = new LinkedList<Integer>();
        for(int i = 0 ; i < bestAlternatives.size() ; i++)
        {
            
            selectedAlternative = bestAlternatives.get(i);
            if(echo){
                System.out.println(selectedAlternative.key+" WAS AMONG CHOSEN ALTERNATIVES");
            }
            indexesOfTheBestAlternatives.addLast(selectedAlternative.key);
        }

        return indexesOfTheBestAlternatives;
    }

    /**
     * @param decision the decision to set
     */
    public void setDecisionMatrixes(Matrix[] decision) {
        this.decisionMatrixes = decision;
    }

    /**
     * @param weights the weights to set
     */
    public void setWeights(Matrix[] weights) {
        this.criteriaWeightMatrixes = weights;
    }

    public static void main(String[] args)
    {
        int d = 5, M = 10, N = 5;
        float veto = (float)0;
        Matrix[] m = new Matrix[d], w = new Matrix[d];
        for (int i = 0; i < d; i++) {
            m[i] = new Matrix<Float>(M, N);
            w[i] = new Matrix<Float>(N, 1);
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < N; k++) {
                    m[i].setElement((float) Math.round((float) (Math.random() * 10)), j, k);
                    w[i].setElement((float) Math.round((float) (Math.random() * 10)), k, 0);
                }
            }
            System.out.println("**M**");
            System.out.println(m[i].toString());
            System.out.println("**W**");
            System.out.println(w[i].toString());
        }
        VIKOR v = new VIKOR();
        v.setEcho(true);
        v.setDecisionMatrixes(m);
        v.setWeights(w);
        v.setVeto(veto);
        v.trace();
    }

    /**
     * @param echo the echo to set
     */
    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    /**
     * @return the veto
     */
    public float getVeto() {
        return veto;
    }

    /**
     * @param veto the veto to set
     */
    public void setVeto(float veto) {
        this.veto = veto;
    }
}

class TempForSort<T>{
    public T value ;
    public int key;

    public TempForSort(int keyIn , T valueIn){
        key = keyIn;
        value = valueIn;
    }

    public String toString(){
        return "["+key+"]=>"+value+",";
    }

}