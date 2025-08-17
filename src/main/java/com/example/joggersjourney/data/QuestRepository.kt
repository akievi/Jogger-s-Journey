package com.example.joggersjourney.data

import com.example.joggersjourney.data.models.Quest
import com.example.joggersjourney.data.models.QuestCategory

object QuestRepository {
    
    fun getQuestById(questId: String): Quest? {
        return getAllQuests().find { it.id == questId }
    }
    
    fun getAllQuests(): List<Quest> {
        return listOf(
            // Easy Quests - Anfänger (7-8 min/km = ~2.1-2.4 m/s)
            Quest(
                id = "easy_jogger1",
                title = "ANFÄNGER JOGGER",
                description = "Laufe 1 km in unter 8 Minuten",
                category = QuestCategory.EASY,
                targetDistance = 1.0,
                targetTime = 8, // minutes
                requiredSpeed = 2.1, // m/s (7.5 km/h)
                rewardGold = 25,
                rewardXP = 30,
                npcName = "Max der Jogger",
                npcDialogueIntro = listOf(
                    "Hey! Bereit für eine kleine Jogging-Runde?",
                    "Ich bin Max und liebe entspanntes Laufen!",
                    "Lass uns gemeinsam 1 km laufen. Schaffst du es in unter 8 Minuten?"
                ),
                npcDialogueWin = listOf(
                    "Wow! Das war richtig gut gelaufen!",
                    "Du hast Talent fürs Joggen!",
                    "Super Zeit! Ich bin beeindruckt!",
                    "Perfekt! Du bist ein echter Läufer!",
                    "Fantastisch! Das war eine starke Leistung!"
                ),
                npcDialogueLoose = listOf(
                    "Keine Sorge, beim nächsten Mal klappt es bestimmt!",
                    "Hey, das war trotzdem ein guter Versuch!",
                    "Übung macht den Meister - weitermachen!",
                    "Das war schon mal ein guter Anfang!",
                    "Kopf hoch! Jeder fängt mal klein an!"
                ),
                latitude = 51.02847,
                longitude = 7.56234
            ),
            
            Quest(
                id = "easy_collector",
                title = "NATUR-GENIEßER",
                description = "Sammle Punkte beim Laufen am See entlang",
                category = QuestCategory.EASY,
                targetDistance = 0.8,
                targetTime = 0, // No time limit
                requiredSpeed = 1.7, // m/s (6 km/h)
                rewardGold = 20,
                rewardXP = 35,
                npcName = "Emma die Sammlerin",
                npcDialogueIntro = listOf(
                    "Hallo! Ich sammle gerne Erinnerungen beim Laufen!",
                    "Möchtest du mit mir eine entspannte Runde am See drehen?",
                    "Lass uns 800m laufen und die Natur genießen!"
                ),
                npcDialogueWin = listOf(
                    "Das war eine wunderbare Runde!",
                    "Du läufst so entspannt - das gefällt mir!",
                    "Perfekt! Wir haben eine tolle Zeit gehabt!",
                    "Großartig! Das war sehr entspannend!",
                    "Toll! Du hast ein gutes Tempo gehalten!"
                ),
                npcDialogueLoose = listOf(
                    "Macht nichts! Die Landschaft war trotzdem schön!",
                    "Beim nächsten Mal nehmen wir es etwas langsamer!",
                    "Kein Problem - hauptsache wir waren draußen!",
                    "Das war trotzdem eine schöne Runde!",
                    "Nicht schlimm - übung macht den Meister!"
                ),
                latitude = 51.02756,
                longitude = 7.56445
            ),
            
            Quest(
                id = "easy_explorer",
                title = "PARK-ERKUNDER",
                description = "Erkunde 1.2 km durch den Park",
                category = QuestCategory.EASY,
                targetDistance = 1.2,
                targetTime = 12,
                requiredSpeed = 1.7, // m/s (6 km/h)
                rewardGold = 30,
                rewardXP = 40,
                npcName = "Ben der Entdecker",
                npcDialogueIntro = listOf(
                    "Hey Abenteurer! Bereit den Park zu erkunden?",
                    "Ich kenne hier jeden Winkel! Kommst du mit?",
                    "Lass uns 1.2 km durch die schönsten Ecken laufen!"
                ),
                npcDialogueWin = listOf(
                    "Wahnsinn! Du kennst dich ja aus hier!",
                    "Perfekte Exploration! Du bist ein echter Entdecker!",
                    "Klasse! Das war eine tolle Entdeckungsreise!",
                    "Super! Du hast alle versteckten Wege gefunden!",
                    "Fantastisch! Du bist ein wahrer Park-Experte!"
                ),
                npcDialogueLoose = listOf(
                    "Schade! Aber der Park hat trotzdem Spaß gemacht!",
                    "Kein Problem! Wir haben trotzdem viel gesehen!",
                    "Das war trotzdem eine schöne Runde!",
                    "Nicht schlimm - der Park ist ja auch groß!",
                    "Beim nächsten Mal zeige ich dir eine kürzere Route!"
                ),
                latitude = 51.02945,
                longitude = 7.56123
            ),
            
            // Medium Quest - Fortgeschritten (5-6 min/km = ~2.8-3.3 m/s)
            Quest(
                id = "medium_runner",
                title = "AUSDAUER-LÄUFER",
                description = "Laufe 3 km in unter 18 Minuten",
                category = QuestCategory.MEDIUM,
                targetDistance = 3.0,
                targetTime = 18,
                requiredSpeed = 2.8, // m/s (10 km/h)
                rewardGold = 75,
                rewardXP = 100,
                npcName = "Sarah die Läuferin",
                npcDialogueIntro = listOf(
                    "Zeit für eine echte Herausforderung!",
                    "Ich bin Sarah und liebe intensive Läufe!",
                    "3 km in 18 Minuten - bist du bereit?"
                ),
                npcDialogueWin = listOf(
                    "Unglaublich! Das war eine Spitzenleistung!",
                    "Wow! Du läufst wie ein Profi!",
                    "Perfekt! Du hast echtes Läufer-Talent!",
                    "Sensationell! Das war richtig stark!",
                    "Grandios! Du bist ein echter Ausdauer-Champion!"
                ),
                npcDialogueLoose = listOf(
                    "Das war trotzdem eine gute Leistung!",
                    "Du warst nah dran! Beim nächsten Mal schaffst du es!",
                    "Starker Versuch! Das Tempo wird noch kommen!",
                    "Nicht schlecht! Du hast Potenzial!",
                    "Das war schon sehr gut - nur etwas mehr Tempo fehlt!"
                ),
                latitude = 51.02634,
                longitude = 7.56789
            ),
            
            // Hard Quest - Fortgeschritten (4-5 min/km = ~3.3-4.2 m/s)
            Quest(
                id = "hard_marathon",
                title = "MARATHON-MEISTER",
                description = "Intensive 5 km Strecke in unter 25 Minuten",
                category = QuestCategory.HARD,
                targetDistance = 5.0,
                targetTime = 25,
                requiredSpeed = 3.3, // m/s (12 km/h)
                rewardGold = 150,
                rewardXP = 250,
                npcName = "Marcus der Marathon-König",
                npcDialogueIntro = listOf(
                    "Du denkst du bist bereit für die Königsklasse?",
                    "Ich bin Marcus - Marathon ist mein Leben!",
                    "5 km in 25 Minuten - das ist nur was für Profis!"
                ),
                npcDialogueWin = listOf(
                    "UNGLAUBLICH! Du bist ein wahrer Champion!",
                    "WELTKLASSE! Solche Läufer sehe ich selten!",
                    "PERFEKT! Du läufst wie ein Olympionike!",
                    "SENSATIONELL! Das war Marathon-Niveau!",
                    "GRANDIOS! Du bist der neue Marathon-König!"
                ),
                npcDialogueLoose = listOf(
                    "Respekt! Schon versucht zu haben ist mutig!",
                    "Das war trotzdem eine starke Leistung!",
                    "Du warst gar nicht so weit weg vom Ziel!",
                    "Beim nächsten Mal zeige ich dir ein paar Tricks!",
                    "Das war schon sehr gut für diese Distanz!"
                ),
                latitude = 51.02523,
                longitude = 7.56567
            )
        )
    }
    
    fun getQuestsByCategory(category: QuestCategory): List<Quest> {
        return getAllQuests().filter { it.category == category }
    }
    
    fun getEasyQuests(): List<Quest> = getQuestsByCategory(QuestCategory.EASY)
    fun getMediumQuests(): List<Quest> = getQuestsByCategory(QuestCategory.MEDIUM)
    fun getHardQuests(): List<Quest> = getQuestsByCategory(QuestCategory.HARD)
} 