package info.androidhive.slidingmenu.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import info.androidhive.slidingmenu.NewProductActivity;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.WalletActivity;
import info.androidhive.slidingmenu.library.GlobalClass;
import info.androidhive.slidingmenu.library.UserFunctions;

public class WalletFragment extends Fragment {

	public WalletFragment(){}

    GlobalClass GF; UserFunctions UF;


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        GF = (GlobalClass) getActivity().getApplicationContext();

        //Mit Click auf diesen Button wird ein neues Produkt ertellt

        /*Button btnneu = (Button) rootView.findViewById(R.id.btnneu);
        btnneu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launching Belegungsplanung
                Intent i = new Intent(getActivity(), NewProductActivity.class);
                startActivity(i);
            }
        }); */

        /*ImageView imgWallet = (ImageView) rootView.findViewById(R.id.imgWallet);
        imgWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WalletActivity.class);
                startActivity(i);
            }
        }); */
         
        return rootView;
    }
}
