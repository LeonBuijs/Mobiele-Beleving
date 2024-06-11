package com.example.androidapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Achievements extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Achievement> achievements = new ArrayList<>();
    private AchievementAdapter achievementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.JohanEnDeEenhoorn);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        displayItems();
    }

    public void setMainScreen(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void setScoreScreen(View view) {
        startActivity(new Intent(this, Scoreview.class));
    }

    public void setAchievementScreen(View view) {
    }

    private void displayItems() {
        achievements.add(new Achievement(R.drawable.eerste_slag1, "Eerste Slag", "Gefeliciteerd met je eerste slag! \nWelkom bij het avontuur van Johan en de Eenhoorn.", false));
        achievements.add(new Achievement(R.drawable.verbonden_krijger2, "Verbonden Krijger", "Je hebt je verbonden met de kracht van de high striker! Laat de slagen beginnen.", true));
        achievements.add(new Achievement(R.drawable.groeimaker_3, "Groeimaker", "Je blijft maar groeien! Blijf die scores verbeteren.", false));
        achievements.add(new Achievement(R.drawable.nieuwsgierige_avonturier_4, "Nieuwsgierige Avonturier", "Je hebt de app verkend! Nu weet je waar alles te vinden is.", false));
        achievements.add(new Achievement(R.drawable.consistente_smasher_5, "Consistente Smasher", "Je bent goed op weg! Blijf slaan en verbeteren.", false));
        achievements.add(new Achievement(R.drawable.dagelijkse_kampioen_6, "Dagelijkse Kampioen", "Vandaag ben jij de kampioen! Laat je vrienden zien wie de baas is.", false));
        achievements.add(new Achievement(R.drawable.tien_op_rij_7, "Tien Op Rij", "Je bent niet te stoppen! Wat een doorzettingsvermogen.", false));
        achievements.add(new Achievement(R.drawable.magische_mepper_6, "Magische Mepper", "Wat een kracht! Je hebt een magische slag in je.", false));
        achievements.add(new Achievement(R.drawable.krachtpatser_9, "Krachtpatser", "Je kracht is ongeëvenaard! Houd dit vol.", false));
        achievements.add(new Achievement(R.drawable.high_striker_meester_10, "High Striker Meester", "Je hebt de high striker onder de knie! Fantastisch werk.", false));
        achievements.add(new Achievement(R.drawable.legendarische_smasher_11, "Legendarische Smasher", "Je hebt geschiedenis geschreven! Iedereen zal jouw naam herinneren.", false));
        achievements.add(new Achievement(R.drawable.onverslaanbaar_12, "Onverslaanbaar", "Niemand kan je stoppen! Je bent een levende legende.", false));
        achievements.add(new Achievement(R.drawable.marathon_smasher_13, "Marathon Smasher", "Je bent een echte volhouder! Geweldige prestatie.", false));
        achievements.add(new Achievement(R.drawable.teamplayer_14, "Teamplayer", "Samen slaan is altijd leuker! Bedankt voor het uitnodigen van je vrienden.", false));
        achievements.add(new Achievement(R.drawable.social_star_15, "Social Star", "Je scores gaan viral! Deel de magie van Johan en de Eenhoorn.", false));
        achievements.add(new Achievement(R.drawable.earlybird_16, "Early Bird", "Vroeg uit de veren en vol energie! Goed gedaan.", false));
        achievements.add(new Achievement(R.drawable.night_owl_17, "Night Owl", "Nachtelijke avonturier! Je energie kent geen grenzen.", false));
        achievements.add(new Achievement(R.drawable.feestbeest_18, "Feestbeest", "Wat een feestelijke slag! Fijne feestdag en blijf genieten.", false));
        achievements.add(new Achievement(R.drawable.uitdager_19, "Uitdager", "Je hebt je vriend verslagen! De uitdaging is gewonnen.", false));
        achievements.add(new Achievement(R.drawable.reisgenoot_20, "Reisgenoot", "Je hebt de high striker op verschillende plekken ervaren! Wat een reis.", false));

        recyclerView = findViewById(R.id.recyclerViewAchievements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        achievementAdapter = new AchievementAdapter(this, achievements);
        achievementAdapter.setTheme(2); // Thema 1 = Groen, Thema 2 = Roze
        recyclerView.setAdapter(achievementAdapter);
    }
}
