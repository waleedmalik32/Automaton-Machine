
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


public class inputChecker{

	Set<State> added = new HashSet<State>();
	static State matchstate = new State(statev.Match._v, null, null);

	enum statev{
		Match(256), 
		Split(257);
		int _v;
		statev(int v){ _v = v;}
		};

	static char[] re2port(String re){
		int nalt = 0, natom = 0;
		char [] buf = new char[8000];
		int iBuf = 0;

		A[] parent = new A[100];
		for(int i = 0; i < parent.length; i ++){
			parent[i] = new A();
		}
		int iP = 0;
		char[] input = re.toCharArray();
		if(re.length() >= buf.length/2)
			return null;
		for(int i = 0; i < input.length; i++){
			System.out.print(input[i]);
			switch(input[i]){
				case '(':
					if(natom > 1){
						--natom;
						buf[iBuf++]='.';
					}
					if( iP >= parent.length)
						return null;
					parent[iP].nalt = nalt;
					parent[iP].natom = natom;
					iP++;
					nalt = 0;
					natom = 0;
					break;
				case '|':
					if(natom == 0)
						return null;
					while(--natom > 0){
						buf[iBuf++] = '.';
					}
					nalt++;
					break;
				case ')':
					if(iP == parent.length)
						return null;
					if(natom == 0)
						return null;
					while(--natom > 0){
						buf[iBuf++] = '.';
					}
					for(;nalt > 0; nalt--){
						buf[iBuf++] = '|';
					}
					--iP;
					nalt = parent[iP].nalt;
					natom = parent[iP].natom;
					natom++;
					break;
				case '*':
				case '+':
				case '?':
					if(natom == 0){
						return null;
					}
					buf[iBuf++] = input[i];
					break;
				default:
					if(natom > 1){
						--natom;
						buf[iBuf++] = '.';
					}
					buf[iBuf++] = input[i];
					natom++;
					break;
			}
		}
		System.out.print(" --> ");
		if( iP != 0) return null;
		while(--natom>0) buf[iBuf++] = '.';
		for(;nalt > 0 ; nalt--) buf[iBuf++] = '|';
		buf[iBuf] = 0;
		return buf;
	}

	State post2nfa( char[] postfix){
		Frag[] stack = new Frag[1000];
		Frag e1, e2, e;
		State s;

		int iStack = 0;

		if( postfix == null ) return null;
		for(int i = 0 ; postfix[i] != 0; i++){
			char p = postfix[i] ;
			System.out.print(p+"'");
			switch (p){
				default:
					s = new State(p, null, null);
					stack[iStack++] = new Frag(s, s.out);
					break;
				case '.':
					e2 = stack[--iStack];
					e1 = stack[--iStack];
					e1.patch(e2.start);
					stack[iStack++] = new Frag(e1.start, e2.outs);
					break;
				case '|':
					e2 = stack[--iStack];
					e1 = stack[--iStack];
					s = new State(statev.Split._v, e1.start, e2.start);
					stack[iStack++] = new Frag(s, append(e1.outs, e2.outs));
					break;
				case '?':
					e = stack[--iStack];
					s = new State(statev.Split._v, e.start, null);
					stack[iStack++] = new Frag(s, append(e.outs, s.out1));
					break;
				case '*':
					e = stack[--iStack];
					s = new State(statev.Split._v, e.start, null);
					e.patch(s);
					stack[iStack++] = new Frag(s, s.out1);
				case '+':
					e = stack[--iStack];
					s = new State(statev.Split._v, e.start, null);
					e.patch(s);
					stack[iStack++] = new Frag(e.start, s.out1);
					break;
			}
		}

		e = stack[--iStack];
		if(iStack != 0){
			System.err.println("Stack pointer is not 0");
		}

		e.patch(matchstate);
		return e.start;

	}

	public List<Arrow> append(List<Arrow> e1, List<Arrow> e2){
		List<Arrow> e = new LinkedList<Arrow>();
		e.addAll(e1);
		e.addAll(e2);
		return e;
	}

	public List<Arrow> append(List<Arrow> e1, Arrow e2){
		List<Arrow> e = new LinkedList<Arrow>();
		e.addAll(e1);
		e.add(e2);
		return e;
	}

	public static boolean isMatch(List<State> l){
		for(State s: l){
			if(s == matchstate)
				System.out.println("matched");
				return true;
		}
		System.out.println("not matched");
		return false;
	}

	public void addState(List l, State s){
		if(s == null || added.contains(s)){
			System.err.println("list id is not correct");
		}
		if(s.c == statev.Split._v){
			addState(l, s.out.peer);
			addState(l, s.out1.peer);
		}
		l.add(s);
	}

	void step(List<State> clist, int c, List<State> nlist){
		added.clear();
		for(State s : clist){
			if(s.c == c){
				addState(nlist, s.out.peer);
				added.add(s);
			}
		}
		//System.out.println("added: "+added);
	}

	boolean match(State start, String ms){
		System.out.println("\nDo Match: "+ms);
		List<State> clist = new LinkedList<State>(), nlist = new LinkedList<State>(), t = null;
		addState(clist, start);
		char[] s = ms.toCharArray();
		for(int index = 0; index < s.length ; index++){
			int c = s[index];
			step(clist, c, nlist);
			clist.clear();
			t = clist; clist = nlist; nlist = t;
		}
		return isMatch(clist);
	}

	public static boolean parseInput(String regex,String input) {
		
		inputChecker j = new inputChecker();
		char[] post = j.re2port(regex);
		State s = j.post2nfa(post);
		return j.match(s, input);
	}

	public static void displayDiagram(State start){
		Set<State> added = new HashSet<State>();
		Set<State> a = new HashSet<State>();
		Set<State> b = new HashSet<State>();
		Set<State> t = null;
		a.add(start);
		added.add(start);
		while(!a.isEmpty()){
			for(State s : a){
				if (s == null) continue;
				System.out.println(s+" -> "+s.out+" \n -> "+s.out1);
				if(!added.contains(s.out.peer)){
					b.add(s.out.peer);
					added.add(s.out.peer);	
				}
				if(!added.contains(s.out1.peer)){
					b.add(s.out1.peer);
					added.add(s.out1.peer);	
				}
				
			}
			a.clear();
			t = a;
			a = b;
			b = t;
		}

	}

	static class A{
		int nalt = 0;
		int natom = 0;
	}

	static class State{
		static int nstate;
		int c;
		Arrow out;
		Arrow out1;

		public State(int c, State out, State out1){
			nstate++;
			this.c = c;
			this.out = new Arrow(out);
			this.out1 = new Arrow(out1);
		}

		public String toString2(){
			return "\nState: nstate:"+nstate+" c:"+c+" out:"+out+" out1:"+out1;
		}
	}

	static class Arrow{
		State peer;
		public Arrow(State peer){
			this.peer = peer;
		}

		public String toString(){
			return peer != null ? peer.toString() : "null";
		}
	}

	static class Frag{
		State start;
		List<Arrow> outs = new LinkedList<Arrow>();

		public Frag(State s, List<Arrow> p){
			start = s;
			outs.addAll(p);
		}
		public Frag(State s, Arrow p){
			start = s;
			outs.add(p);
		}

		public void patch(State s){
			for(Arrow o : outs){
				o.peer = s;
			}
		}
	}
	
	// end 
		
	
}