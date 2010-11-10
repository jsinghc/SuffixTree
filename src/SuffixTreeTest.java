
import com.cowinvest.lib.suffixTree.*;

public class SuffixTreeTest {
	public static void main (String[] args) {
		SuffixTree suffixTree = new SuffixTree("tabcdabeabmtestforbestpest");
		boolean btest = suffixTree.search("btest");
		boolean pest = suffixTree.search("pest");
		boolean beabmtest = suffixTree.search("beabmtest");
		System.out.print("btest result:" + btest);
		System.out.print("\nPest result:" + pest);
		System.out.print("\nbeabmtest result:" + beabmtest + "\n");
	}
}
