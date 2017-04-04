package developers.findingcodes.kurtisgallery;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {



    private Context mContext;

    public TabAdapter(Context context, FragmentManager fm){ //Constructor
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) { //getting the fragment at required position..
        if (position == 0) {
            return new FragmentNewArrival();
        } else if (position == 1) {
            return new AKPremiumOneFragment();
        } else if(position == 2){
            return new AKPremiumTwoFragment();
        }else{
            return new AKVSeriesOneFragment();
        }
    }

    @Override
    public int getCount() { //getting the total no of item..
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) { //Getting the page title
        if (position == 0) {
            return mContext.getString(R.string.fragment_1_title);
        } else if (position == 1) {
            return mContext.getString(R.string.fragment_2_title);
        } else if(position == 2) {
            return mContext.getString(R.string.fragment_3_title);
        }else{
            return mContext.getString(R.string.fragment_4_title);
        }
    }


}
