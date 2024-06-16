package com.example.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

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
    private TextView textAchievementOverview;
    private SharedPreferences sharedPreferences;
    private int appTheme = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("THEME","2");
//        editor.apply();
        if (sharedPreferences != null) {
            if (sharedPreferences.getString("THEME", null) != null) {
                if (sharedPreferences.getString("THEME", null).equals("1")) {
                    setTheme(R.style.Cobra);
                    this.appTheme = 1;
                } else if (sharedPreferences.getString("THEME", null).equals("2")) {
                    setTheme(R.style.JohanEnDeEenhoorn);
                    this.appTheme = 2;
                }
            } else {
                setTheme(R.style.Cobra);
                this.appTheme = 1;
            }
        } else {
            System.out.println("null");
            setTheme(R.style.Cobra);
            this.appTheme = 1;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        textAchievementOverview = findViewById(R.id.textAchievementOverview);

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
        achievements.add(new Achievement(R.drawable.eerste_slag1, getString(R.string.eerste_slag), getString(R.string.gefeliciteerd_met_je_eerste_slag_welkom_bij_het_avontuur_van_johan_en_de_eenhoorn), true));
        achievements.add(new Achievement(R.drawable.verbonden_krijger2, getString(R.string.verbonden_krijger), getString(R.string.je_hebt_je_verbonden_met_de_kracht_van_de_high_striker_laat_de_slagen_beginnen), true));
        achievements.add(new Achievement(R.drawable.groeimaker_3, getString(R.string.groeimaker), getString(R.string.je_blijft_maar_groeien_blijf_die_scores_verbeteren), false));
        achievements.add(new Achievement(R.drawable.nieuwsgierige_avonturier_4, getString(R.string.nieuwsgierige_avonturier), getString(R.string.je_hebt_de_app_verkend_nu_weet_je_waar_alles_te_vinden_is), false));
        achievements.add(new Achievement(R.drawable.consistente_smasher_5, getString(R.string.consistente_smasher), getString(R.string.je_bent_goed_op_weg_blijf_slaan_en_verbeteren), false));
        achievements.add(new Achievement(R.drawable.dagelijkse_kampioen_6, getString(R.string.dagelijkse_kampioen), getString(R.string.vandaag_ben_jij_de_kampioen_laat_je_vrienden_zien_wie_de_baas_is), false));
        achievements.add(new Achievement(R.drawable.tien_op_rij_7, getString(R.string.tien_op_rij), getString(R.string.je_bent_niet_te_stoppen_wat_een_doorzettingsvermogen), true));
        achievements.add(new Achievement(R.drawable.magische_mepper_6, getString(R.string.magische_mepper), getString(R.string.wat_een_kracht_je_hebt_een_magische_slag_in_je), false));
        achievements.add(new Achievement(R.drawable.krachtpatser_9, getString(R.string.krachtpatser), getString(R.string.je_kracht_is_onge_venaard_houd_dit_vol), false));
        achievements.add(new Achievement(R.drawable.high_striker_meester_10, getString(R.string.high_striker_meester), getString(R.string.je_hebt_de_high_striker_onder_de_knie_fantastisch_werk), false));
        achievements.add(new Achievement(R.drawable.legendarische_smasher_11, getString(R.string.legendarische_smasher), getString(R.string.je_hebt_geschiedenis_geschreven_iedereen_zal_jouw_naam_herinneren), false));
        achievements.add(new Achievement(R.drawable.onverslaanbaar_12, getString(R.string.onverslaanbaar), getString(R.string.niemand_kan_je_stoppen_je_bent_een_levende_legende), false));
        achievements.add(new Achievement(R.drawable.marathon_smasher_13, getString(R.string.marathon_smasher), getString(R.string.je_bent_een_echte_volhouder_geweldige_prestatie), false));
        achievements.add(new Achievement(R.drawable.teamplayer_14, getString(R.string.teamplayer), getString(R.string.samen_slaan_is_altijd_leuker_bedankt_voor_het_uitnodigen_van_je_vrienden), false));
        achievements.add(new Achievement(R.drawable.social_star_15, getString(R.string.social_star), getString(R.string.je_scores_gaan_viral_deel_de_magie_van_johan_en_de_eenhoorn), false));
        achievements.add(new Achievement(R.drawable.earlybird_16, getString(R.string.early_bird), getString(R.string.vroeg_uit_de_veren_en_vol_energie_goed_gedaan), false));
        achievements.add(new Achievement(R.drawable.night_owl_17, getString(R.string.night_owl), getString(R.string.nachtelijke_avonturier_je_energie_kent_geen_grenzen), true));
        achievements.add(new Achievement(R.drawable.feestbeest_18, getString(R.string.feestbeest), getString(R.string.wat_een_feestelijke_slag_fijne_feestdag_en_blijf_genieten), true));
        achievements.add(new Achievement(R.drawable.uitdager_19, getString(R.string.uitdager), getString(R.string.je_hebt_je_vriend_verslagen_de_uitdaging_is_gewonnen), false));
        achievements.add(new Achievement(R.drawable.reisgenoot_20, getString(R.string.reisgenoot), getString(R.string.je_hebt_de_high_striker_op_verschillende_plekken_ervaren_wat_een_reis), true));

        recyclerView = findViewById(R.id.recyclerViewAchievements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        achievementAdapter = new AchievementAdapter(this, achievements);
        achievementAdapter.setTheme(this.appTheme); // Thema 1 = Groen, Thema 2 = Roze
        recyclerView.setAdapter(achievementAdapter);

        updateAchievementOverview();
    }

    private void updateAchievementOverview() {
        int achievedCount = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isAchieved()) {
                achievedCount++;
            }
        }
        String overviewText = getString(R.string.achievement_overview, achievedCount, achievements.size());
        textAchievementOverview.setText(overviewText);
    }
}