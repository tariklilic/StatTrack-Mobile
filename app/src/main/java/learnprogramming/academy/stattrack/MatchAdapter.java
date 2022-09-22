package learnprogramming.academy.stattrack;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// used for filling up ListView for matches (display matches)
public class MatchAdapter extends BaseAdapter {
    private java.util.List<Match> matchList;
    private Context context;
    int blue = Color.parseColor("#0D6EFD");
    int red = Color.parseColor("#DC3545");

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int i) {
        return matchList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return matchList.get(i).getId();
    }

    public MatchAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.activity_match_view, viewGroup, false);
        view.setBackgroundColor(red);
        Match match = matchList.get(position);

        ImageView championIcon = view.findViewById(R.id.championId);
        ImageView item0 = view.findViewById(R.id.item0ImageView);
        ImageView item1 = view.findViewById(R.id.item1ImageView);
        ImageView item2 = view.findViewById(R.id.item2ImageView);
        ImageView item3 = view.findViewById(R.id.item3ImageView);
        ImageView item4 = view.findViewById(R.id.item4ImageView);
        ImageView item5 = view.findViewById(R.id.item5ImageView);
        ImageView item6 = view.findViewById(R.id.item6ImageView);
        ImageView spell1 = view.findViewById(R.id.spell1ImageView);
        ImageView spell2 = view.findViewById(R.id.spell2ImageView);

        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(championIcon);
        imageViews.add(item0);
        imageViews.add(item1);
        imageViews.add(item2);
        imageViews.add(item3);
        imageViews.add(item4);
        imageViews.add(item5);
        imageViews.add(item6);
        imageViews.add(spell1);
        imageViews.add(spell2);

        setIcons(match, imageViews);

        TextView matchResult = view.findViewById(R.id.matchResultText);
        TextView killsDeathsAssists = view.findViewById(R.id.killsDeathsAssistsText);
        TextView kda = view.findViewById(R.id.kdaText);
        TextView controlWardsPlaced = view.findViewById(R.id.controlWardsPlacedText);
        TextView wardsKilled = view.findViewById(R.id.wardsKilledText);
        TextView wardsPlaced = view.findViewById(R.id.wardsPlacedText);
        TextView damageDealt = view.findViewById(R.id.totalDamageDealtText);
        TextView damageTaken = view.findViewById(R.id.totalDamageTakenText);
        TextView minionsKilled = view.findViewById(R.id.minionsKilledText);

        matchResult.append(match.getMatchResult());
        killsDeathsAssists.append(match.getKillsDeathsAssists());
        kda.append(Double.toString(match.getKda()));
        controlWardsPlaced.append(Integer.toString(match.getControlWardsPlaced()));
        wardsKilled.append(Integer.toString(match.getWardsKilled()));
        wardsPlaced.append(Integer.toString(match.getWardsPlaced()));
        damageDealt.append(Integer.toString(match.getDamageDealt()));
        damageTaken.append(Integer.toString(match.getDamageTaken()));
        minionsKilled.append(Integer.toString(match.getMinionsKilled()));

        if (matchResult.getText().toString().equals("Victory")){ //setting correct match view color
            view.setBackgroundColor(blue);
        }

        return view;
    }

    // populates all ImageViews with images (champion icon -> 0, items -> 1-6, summoner spells -> 8-9)
    private void setIcons(Match match, ArrayList<ImageView> imageViews){
        String iconName = match.getChampionIcon().toLowerCase();
        // gets imageId (corresponding android int value) based on champ name
        int imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        // sets image based on previously retrieved imageId
        imageViews.get(0).setImageResource(imageId);
        // sets tag which is later used to extract file for voiceline based on name (example: vayne.ogg)
        imageViews.get(0).setTag(iconName);

        iconName = match.getItems().get(0);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(1).setImageResource(imageId);

        iconName = match.getItems().get(1);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(2).setImageResource(imageId);

        iconName = match.getItems().get(2);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(3).setImageResource(imageId);

        iconName = match.getItems().get(3);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(4).setImageResource(imageId);

        iconName = match.getItems().get(4);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(5).setImageResource(imageId);

        iconName = match.getItems().get(5);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(6).setImageResource(imageId);

        iconName = match.getItems().get(6);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(7).setImageResource(imageId);

        iconName = match.getSpells().get(0);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(8).setImageResource(imageId);

        iconName = match.getSpells().get(1);
        imageId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        imageViews.get(9).setImageResource(imageId);

    }

}