package com.ai.viterbi;

import java.util.Enumeration;
import java.util.Hashtable;

public class Viterbi {
	static final String S1 = "S1";
	static final String S2 = "S2";
	static final String S3 = "S3";
	static final String S4 = "S4";

	/*
	 * static final String Zero = "0"; static final String One = "1";
	 */

	public static void main(String[] args) {
		String[] states = new String[] { S1, S2, S3, S4 };

		//int[] observations = new int[] { 0, 1, 1, 0 };

		Hashtable<String, Float> start_probability = new Hashtable<String, Float>();
		start_probability.put(S1, 0.3f);
		start_probability.put(S2, 0.4f);
		start_probability.put(S3, 0.2f);
		start_probability.put(S4, 0.1f);

		// transition_probability for action A1
		Hashtable<String, Hashtable<String, Float>> transition_probability_a1 = new Hashtable<String, Hashtable<String, Float>>();
		Hashtable<String, Float> t1 = new Hashtable<String, Float>();
		t1.put(S1, 0.4f);
		t1.put(S2, 0.3f);
		t1.put(S3, 0.1f);
		t1.put(S4, 0.2f);

		Hashtable<String, Float> t2 = new Hashtable<String, Float>();
		t2.put(S1, 0.4f);
		t2.put(S2, 0.2f);
		t2.put(S3, 0.2f);
		t2.put(S4, 0.2f);

		Hashtable<String, Float> t3 = new Hashtable<String, Float>();
		t3.put(S1, 0.5f);
		t3.put(S2, 0.1f);
		t3.put(S3, 0.1f);
		t3.put(S4, 0.3f);

		Hashtable<String, Float> t4 = new Hashtable<String, Float>();
		t4.put(S1, 0.3f);
		t4.put(S2, 0.4f);
		t4.put(S3, 0.1f);
		t4.put(S4, 0.2f);

		transition_probability_a1.put(S1, t1);
		transition_probability_a1.put(S2, t2);
		transition_probability_a1.put(S3, t3);
		transition_probability_a1.put(S4, t4);

		// transition_probability for action A2
		Hashtable<String, Hashtable<String, Float>> transition_probability_a2 = new Hashtable<String, Hashtable<String, Float>>();
		Hashtable<String, Float> t5 = new Hashtable<String, Float>();
		t5.put(S1, 0.1f);
		t5.put(S2, 0.2f);
		t5.put(S3, 0.3f);
		t5.put(S4, 0.4f);

		Hashtable<String, Float> t6 = new Hashtable<String, Float>();
		t6.put(S1, 0.4f);
		t6.put(S2, 0.1f);
		t6.put(S3, 0.3f);
		t6.put(S4, 0.2f);

		Hashtable<String, Float> t7 = new Hashtable<String, Float>();
		t7.put(S1, 0.2f);
		t7.put(S2, 0.1f);
		t7.put(S3, 0.1f);
		t7.put(S4, 0.6f);

		Hashtable<String, Float> t8 = new Hashtable<String, Float>();
		t8.put(S1, 0.3f);
		t8.put(S2, 0.3f);
		t8.put(S3, 0.3f);
		t8.put(S4, 0.1f);

		transition_probability_a2.put(S1, t5);
		transition_probability_a2.put(S2, t6);
		transition_probability_a2.put(S3, t7);
		transition_probability_a2.put(S4, t8);

		// emission_probability
		Hashtable<String, Hashtable<Integer, Float>> emission_probability = new Hashtable<String, Hashtable<Integer, Float>>();

		Hashtable<Integer, Float> e1 = new Hashtable<Integer, Float>();
		e1.put(0, 0.6f);
		e1.put(1, 0.4f);

		Hashtable<Integer, Float> e2 = new Hashtable<Integer, Float>();
		e2.put(0, 0.4f);
		e2.put(1, 0.6f);

		Hashtable<Integer, Float> e3 = new Hashtable<Integer, Float>();
		e3.put(0, 0.2f);
		e3.put(1, 0.8f);

		Hashtable<Integer, Float> e4 = new Hashtable<Integer, Float>();
		e4.put(0, 0.9f);
		e4.put(1, 0.1f);

		emission_probability.put(S1, e1);
		emission_probability.put(S2, e2);
		emission_probability.put(S3, e3);
		emission_probability.put(S4, e4);

		Object[] obser = state_Generation(states, start_probability,
				transition_probability_a1, transition_probability_a2,
				emission_probability);
		System.out.println("State transition is : " + obser[0]);
		int[] ob = (int[]) obser[1];
		for (int i = 0; i < 10; i++)
			System.out.println("emited signal is : " + ob[i]);

		Object[] ret = forward_viterbi(ob, states, start_probability,
				transition_probability_a1, transition_probability_a2,
				emission_probability);
		//System.out.println(((Float) ret[0]).floatValue());
		System.out.println("Predicted state transition :" + (String) ret[1]);
		//System.out.println(((Float) ret[2]).floatValue());
	}

	public static Object[] state_Generation(String[] states,
			Hashtable<String, Float> start_p,
			Hashtable<String, Hashtable<String, Float>> trans_p_a1,
			Hashtable<String, Hashtable<String, Float>> trans_p_a2,
			Hashtable<String, Hashtable<Integer, Float>> emit_p) {
		int length = 10;
		int observation[] = new int[length];
		StringBuffer state_Transition = new StringBuffer();
		Double probability = 0.00;
		String source_state = null;
		String next_state = null;
		int total_states = 4;
		int observation_index = 0;
		Double startTest = 0.0; 
		Double startProbability = Math.random();
		for (int j = 0; j < total_states; j++) {
			String option = "S" + (j + 1);
			//System.out.println("Option state : " + option);
			startTest += (double) start_p.get(option);
			if (startProbability <= startTest) {
				source_state = option;
				break;
			}
		}
		Hashtable<String, Hashtable<String, Float>> trans_p = null;
		state_Transition.append(source_state);
		while (length > 0) {
			probability = Math.random();
			if (probability < 0.5) {
				//System.out.println("Transsition matrix A1");
				trans_p = trans_p_a1;
			} else {
				//System.out.println("Transsition matrix A2");
				trans_p = trans_p_a2;
			}

			double sum = 0;

			// find the next state
			for (int j = 0; j < total_states; j++) {
				String option = "S" + (j + 1);
				//System.out.println("Option state : " + option);
				sum += (double) trans_p.get(source_state).get(option);
				//System.out.println("probability is : " + probability);
				//System.out.println("sum is : " + sum);

				if (probability <= sum) {
					next_state = option;
					state_Transition.append(", " + next_state);
					double emission = emit_p.get(source_state).get(0);
					if (probability < emission)
						observation[observation_index] = 0;
					else
						observation[observation_index] = 1;
					break;
				}
			}
			source_state = next_state;
			next_state = null;
			observation_index++;
			length--;
		}
		return new Object[] { state_Transition, observation };
	}

	public static Object[] forward_viterbi(int[] obs, String[] states,
			Hashtable<String, Float> start_p,
			Hashtable<String, Hashtable<String, Float>> trans_p_a1,
			Hashtable<String, Hashtable<String, Float>> trans_p_a2,
			Hashtable<String, Hashtable<Integer, Float>> emit_p) {

		Double prob_action = 0.00;
		Hashtable<String, Hashtable<String, Float>> trans_p = null;
		Hashtable<String, Object[]> T = new Hashtable<String, Object[]>();
		for (String state : states)
			T.put(state,
					new Object[] { start_p.get(state), state,
							start_p.get(state) });

		for (int output : obs) {
			//System.out.println("For observed output symbol : " + output);
			prob_action = Math.random();
			if (prob_action < 0.5)
				trans_p = trans_p_a1;
			else
				trans_p = trans_p_a2;

			Hashtable<String, Object[]> U = new Hashtable<String, Object[]>();

			for (String next_state : states) {
				//System.out.println("For Next state : " + next_state);

				// probability of being in a state after a transition
				float total = 0;
				String argmax = "";
				float valmax = 0;

				// Probability to be in a given initial state
				float prob = 1;
				String v_path = "";
				float v_prob = 1;

				for (String source_state : states) {
					//System.out.println("For Source state symbol : "
					//		+ source_state);
					Object[] objs = T.get(source_state);
					prob = ((Float) objs[0]).floatValue();
					v_path = (String) objs[1];
					v_prob = ((Float) objs[2]).floatValue();

//					System.out.println("Emission probability : "
//							+ emit_p.get(source_state));
//					System.out.println("Transition probability from Source : "
//							+ source_state + "to next state " + next_state
//							+ " is "
//							+ trans_p.get(source_state).get(next_state));

					float p = emit_p.get(source_state).get(output)
							* trans_p.get(source_state).get(next_state);
//					System.out
//							.println("Probability p for transition from  source state- "
//									+ source_state
//									+ " to next state- "
//									+ next_state + " with Probability :" + p);
					prob *= p;
					v_prob *= p;
					total += prob;
					if (v_prob > valmax) {
						argmax = v_path + ", " + next_state;
						valmax = v_prob;
					}
//					System.out.println("valmax : " + valmax);
//					System.out.println("Argmax : " + argmax);
//					System.out
//							.println("total probability to transition from source state- "
//									+ source_state
//									+ " to next state- "
//									+ next_state
//									+ " includes probability of being in source state : "
//									+ prob);
//					System.out.println("v_prob : " + v_prob);
//					System.out.println("v_path : " + v_path);
//					System.out.println("total : " + total);
				}
				U.put(next_state, new Object[] { total, argmax, valmax });
//				System.out.println("Table U is with key " + next_state
//						+ " values " + total + " , " + argmax + " , " + valmax);
			}
			T = U;
//			System.out.println("values of hashtable T");
//			Enumeration<String> keys = T.keys();
//			while (keys.hasMoreElements()) {
//				String key = keys.nextElement();
//				Object[] obj = T.get(key);
//				System.out.println("key : " + key + "values " + obj[0] + " , "
//						+ obj[1] + " , " + obj[2]);
//			}
		}
		//System.out.println("Final values of hashtable T");
		//Enumeration<String> keys = T.keys();
//		while (keys.hasMoreElements()) {
//			String key = keys.nextElement();
//			Object[] obj = T.get(key);
//			System.out.println("key : " + key + "values " + obj[0] + " , "
//					+ obj[1] + " , " + obj[2]);
//		}

		float total = 0;
		String argmax = "";
		float valmax = 0;

		float prob;
		String v_path;
		float v_prob;

		System.out.println("After all the calculation");
		for (String state : states) {
			//System.out.println("For State : " + state);
			Object[] objs = T.get(state);
			prob = ((Float) objs[0]).floatValue();
			v_path = (String) objs[1];
			v_prob = ((Float) objs[2]).floatValue();
			total += prob;
//			System.out.println("v_prob : " + v_prob);
//			System.out.println("v_path : " + v_path);
//			System.out.println("total : " + total);
//			System.out.println("valmax : " + valmax);
			if (v_prob > valmax) {
				argmax = v_path;
				valmax = v_prob;
			}
//			System.out.println("valmax : " + valmax);
//			System.out.println("Argmax : " + argmax);
		}
		return new Object[] { total, argmax, valmax };
	}
}
