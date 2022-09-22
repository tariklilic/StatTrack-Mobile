package learnprogramming.academy.stattrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;

// Broadcast used for daily reminder only, not for event-based notification (example: "match loaded")
public class NotificationBroadcast extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //	FLAG_ACTIVITY_CLEAR_TOP
        // If set, and the activity being launched is already running in the current task,
        // then instead of launching a new instance of that activity, all of the other activities on top of it
        // will be closed and this Intent will be delivered to the (now on top) old activity as a new Intent.

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Flag indicating that if the described PendingIntent already exists, then keep it but replace
        // its extra data with what is in this new Intent.
        // This can be used if you are creating intents where only the extras change, and don't care that
        // any entities that received your previous PendingIntent will be able to launch it with your new extras
        // even if they are not explicitly given to it.

        // makes the notification interactable (opens Main Activity)
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String tipOfTheDay = selectTipOfTheDay();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.background)
                .setContentTitle("Daily fun fact")
                .setContentText(tipOfTheDay).setSound(alarmSound)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(tipOfTheDay))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setAutoCancel(true);

        // Reminders won't be sent on newer devices without this code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Tips / hints notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("This is my notification!");

            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    // selects 1 random string of the 102 and uses it for the daily notification / reminder
    private String selectTipOfTheDay() {
        Random rand = new Random();
        //length = 102
        String[] tips =
        {
                "The capital city of the Noxian Empire was founded around the Immortal Bastion; the citadel of a dreaded revenant who terrorized Valoran for centuries.",
                "One of Kled's many titles is \"High Major Commodore of the First Legion Third Multiplication Double Admiral Artillery Vanguard Company.\"",
                "The Brackern, a race of enormous, crystal-bodied scorpions, slumber in the sands beneath a hidden valley in Shurima.",
                "Sai Kahleek is one of Shurima's most feared deserts, as it is populated by the ravenous Xer'Sai.",
                "Many Voidborn have names originating from the ancient Shuriman language.",
                "The God-Willow was an ancient tree found in Ionia, before being felled by Ivern the Cruel.",
                "The man who preceded Shen as \"Eye of Twilight\" was his father, Kusho.",
                "Much of Bilgewater's export comes from the valuable goods harvested from the carcasses of sea monsters gutted at the Slaughter Docks.",
                "Miss Fortune's guns were crafted by her mother for a young Gangplank, who turned the guns on their creator.",
                "Legends speak of a realm beyond Mount Targon's peak, inhabited by great celestial beings known as the Aspects.",
                "Using lime, ash and the fossilized bark of ancient trees, Demacians create petricite; magic absorbing stone with which they built their walls.",
                "Sometimes known as silver steel or rune steel, Demacian steel is coveted for its durability, light weight and its unique interactions with magic.",
                "Garen and Lux are siblings of the Crownguard family, who traditionally serve as the royal protectors.",
                "Fiora serves as the head of House Laurent in Demacia, a position she claimed upon killing her father in a duel.",
                "When Zaun's plans to create a waterway between east and west Valoran went awry, Janna prevented thousands of deaths and is now revered by many as a goddess.",
                "Many frost trolls have rallied behind the self proclaimed \"Troll King,\" Trundle.",
                "Artifacts of True Ice hold incredible power, but only the Iceborn can wield them.",
                "Before becoming an Ice Witch, Lissandra was permanently blinded by an Ursine's claws.",
                "Trundle's True Ice club is named \"Boneshiver.\"",
                "It is said the passage of time is different in Bandle City, giving yordles a timeless nature.",
                "Because of their glamour, the true form of a yordle is difficult for normal humans to perceive.",
                "Before the Ruination, the Shadow Isles were known as the Blessed Isles.",
                "Mordekaiser's mace is named \"Nightfall.\"",
                "To stave off a great danger beneath the ocean, the Marai illuminate the depths with a moonstone.",
                "The vastaya are chimeric beings who owe their heritage to a union of humans and an ancient, long extinct race.",
                "Basilisks of the Kumungu are sometimes raised from eggs as Noxian war beasts.",
                "Drakehounds, a distant relative of dragons, are kept by some rich, and perhaps foolish, Noxians as pets.",
                "Warmasons are tasked by Noxus not only to plot and plan infrastructure, but also to scout out enemy territory for future invasions.",
                "The sisters Katarina and Cassiopeia are a part of the Du Couteau family; one of the most prestigious noble houses in Noxus.",
                "Native to the rocky crags of northern Demacia are the rare, ferocious Raptors. Only a few individuals have been known to befriend and ride these beasts.",
                "Legends speak of how Avarosa, ancient queen of the Freljord, was killed by her own sister.",
                "The Incognium Runeterra in Piltover was designed to locate anyone in Runeterra, yet its creator mysteriously died before revealing the formula that powers it, leaving it inoperable.",
                "Zaun's wealthiest can often be found on the Promenade level of Zaun, which intersects with Piltover's lowest regions.",
                "The Entresol level of Zaun is where most of the city's trade and business occurs.",
                "The Sump comprises Zaun's lower levels. Most of the working class live here, showcasing their vibrant culture.",
                "Souls claimed by the Black Mist are known to some as \"the Fallen.\" While usually incorporeal, they can be harmed with the right tools, such as magic, silver or even sunlight.",
                "The Blessed Isles were once home to an ancient society of scholars who gathered magical artifacts and historical records from across Runeterra.",
                "Above the city of Nashramae stands a replica Sun Disc, built long ago to honor the lost legacy of ancient Shurima.",
                "In the Shuriman city of Nashramae, a festival is held celebrating Rammus. Thousands gather to roll and somersault around the city in his honor.",
                "Within the bowels of Shurima's capital lies the \"Oasis of the Dawn,\" from which almost all rivers in the land flow from.",
                "Some Shuriman nomads have created entire towns upon the backs of the Dormun; enormous, armor-plated beasts that wander the wastes.",
                "The Shuriman city of Zuretta is ruled by Hierophant Hadiya Nejem, who believes herself to be a descendant of the Ascended warrior Setaka.",
                "In Shurima, those who survived a failed Ascension ritual are known as the Baccai, who often are twisted and malformed.",
                "The Solari revere the sun and denounce all other forms of light as inferior. Their faith dominates the slopes of Mount Targon.",
                "The Rakkor of Mount Targon spend much of their days battling raiders, slaying otherworldly beings, and sparring amongst themselves.",
                "There is a garden in southern Ionia where flowers consume memories.",
                "Garen left home to join the Dauntless Vanguard, one of Demacia's most elite military forces, when he was only twelve years old.",
                "Khada Jhin is a stage name. Jhin's true identity remains a mystery.",
                "In life, Hecarim was the leader of the Iron Order; an infamous brotherhood of mounted knights.",
                "Using enchanted waters buried deep in the earth, Maokai turned the desolate islands he was born on into a verdant paradise.",
                "With his dying breath, Sion killed Jarvan I. The King's crown now serves as Sion's jaw.",
                "Wukong was once known as Kong, but upon besting his teacher in combat, earned an honorific reserved only for the brightest of Wuju students, becoming Wukong.",
                "Rumble's mech is named \"Tristy,\" in honor of a yordle who is very important to him.",
                "Poppy was once close friends with a man named Orlon. She took up his hammer upon his death.",
                "Blitzcrank was created by Viktor to be a stronger, faster and smarter golem; one that could better help the people of Zaun.",
                "Ekko is one of the \"Lost Children of Zaun,\" a band of Zaunite youths known for their mischief across the city.",
                "Illaoi wields the \"Eye of God,\" an idol of one of the gods of Buhru.",
                "The Tidecaller's staff allows its wielder to command water, and is drawn to the power of the moon.",
                "Thanks to her hextech heart, Camille has managed to retain some of her youthful looks despite her age.",
                "Jinx's rocket launcher, mini-gun and electric pistol are named Fishbones, Pow-Pow and Zapper.",
                "Dr. Mundo isn't actually a doctor. He just thinks he is.",
                "Pantheon is one of the Aspects of Targon, and currently inhabits the body of a Rakkoran warrior named Atreus.",
                "Galio was created over a period of two years by a legendary Demacian sculptor named Durand.",
                "Quinn and her twin brother Caleb were born in the rural Demacian town of Uwendale.",
                "Rengar is one of the Kiilash, a vastayan tribe from Shurima.",
                "Nami is one of the Marai; a vastayan tribe from the seas west of Mount Targon.",
                "Xayah and Rakan are of the Lhotlan vastaya, who used to thrive in the Ionian highlands.",
                "Wukong's vastayan tribe, the Shimon, dwells amongst the canopies of Ionia's tallest trees.",
                "Jax's favorite food is eggs, particularly those from a certain caravansary in the Shuriman city of Uzeris.",
                "The people of Bilgewater have a great respect for the monstrous creatures of the deep, so much so, that their currency is Golden Krakens and Silver Serpents.",
                "The Ecliptic Vaults were once considered to be Piltover's most secure bank, before Jinx breached its heavily reinforced walls.",
                "Caitlyn's hextech rifle was crafted by her parents for her twenty-first birthday.",
                "Jayce's transforming hammer is powered by a shard from a Brackern crystal from the depths of the Shuriman sands.",
                "Taric, a former Demacian soldier, is host to one of the Aspects of Targon: The Protector.",
                "It was upon Mount Targon that the Star Forger first met the Targonians, who gave him the name Aurelion Sol.",
                "When the human girl Orianna succumbed to poisonous fumes, her father Corin replaced her failing body parts with mechanical augments, transforming her into a lady of clockwork.",
                "Before becoming a member of the Wardens, Vi wielded a pair of gauntlets taken from a chem-powered mining golem.",
                "The callers of the Serpent Isles sound enormous horns carved into cliffs to beckon forth great sea monsters.",
                "Twisted Fate was born to the nomadic river-folk of the Serpentine, a river flowing near Bilgewater.",
                "Nunu's yeti companion is named Willump.",
                "Darius and Draven are brothers.",
                "The name Tahm Kench was that of a gambler who made a bargain with the River King. That name has since become synonymous with the demon.",
                "When the wharf rats of Bilgewater gather in great swarms, they can devour a grown adult in minutes.",
                "Gateways of dark stone known as Noxtoraa are raised over roads in territories conquered by Noxus.",
                "Hextech is the fusion of magic and technology, harnessing the power contained within extremely rare crystals.",
                "Piltover is the center of mercantile trade in Valoran.",
                "Zaun's powerful Chem-Barons keep a loose alliance that prevents the city from descending into chaos.",
                "The Zaun Gray is the thick, chemical atmosphere of Zaun that can be fatal to breathe where densely settled.",
                "Ionian architecture is known for its harmonious fluidity with nature.",
                "The Black Mist can manifest anywhere in Valoran during the Harrowing, though it strikes Bilgewater most.",
                "Many structures in Bilgewater are made from remnants of ships gutted upon its rocky shores.",
                "In Bilgewater, the dead are not buried - they are given back to the ocean in submerged caskets below bobbing tombstones.",
                "True Ice never melts. Dark Ice is True Ice that has been corrupted.",
                "The Frozen Watchers are believed to be trapped in the Howling Abyss.",
                "Sivir's crossblade, the Chalicar, once belonged to Setaka, an Ascended Warrior Queen of Shurima.",
                "Illaoi was Gangplank's first love.",
                "Fantastical occurrences may indicate where the veil separating Runeterra and the Void has worn thin.",
                "Some say yordles know secret ways through Runeterra others cannot perceive.",
                "The Bearded Lady, Mother Below, and Nagakabouros are all names Bilgewater natives call their deity.",
                "Located far from civilization, Mount Targon is utterly remote, reachable only by the most determined seeker.",
                "Those Ascended by the Sun Disc of Shurima can live for thousands of years.",
                "Demacia's army is often outnumbered, but is arguably the most elite, well-trained army in Runeterra."
        };

        int upperBound = tips.length;
        // 0 - 101 (upperBound-1)
        int randomInt = rand.nextInt(upperBound);

        return tips[randomInt];
    }

}
