package se331.daybreaknews.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import se331.daybreaknews.entity.News;
import se331.daybreaknews.entity.NewsStatus;
import se331.daybreaknews.repository.NewsRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    private final NewsRepository newsRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (newsRepository.count() > 0) return; // already seeded
        seedHardcoded();
    }

    private void seedHardcoded() {
        News n;
        // #1: Local Market Flooded with Expired Goods
        n = News.builder()
                .title("Local Market Flooded with Expired Goods")
                .content("Multiple stalls in the central Sunday market were caught selling expired goods. Local authorities have begun inspections to prevent future incidents.")
                .status(NewsStatus.FAKE)
                .reporter("Jane Smith")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-10T14:23:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/101/800/400")
                .build();
        n = newsRepository.save(n);

        // #2: City Council Approves New Green Park
        n = News.builder()
                .title("City Council Approves New Green Park")
                .content("The city council voted unanimously to convert an unused lot into a green park with walking trails, a pond, and a children's playground.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Michael Lee")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-09T09:10:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/102/800/400")
                .build();
        n = newsRepository.save(n);

        // #3: Schools to Introduce AI Tutors Next Semester
        n = News.builder()
                .title("Schools to Introduce AI Tutors Next Semester")
                .content("Starting next semester, public schools will have access to AI-powered tutoring assistants designed to support teachers and provide personalized student feedback.")
                .status(NewsStatus.UNVERIFIED)
                .reporter("Sara Thompson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-08T16:40:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/103/800/400")
                .build();
        n = newsRepository.save(n);

        // #4: Rumors of Free Public Transport Go Viral
        n = News.builder()
                .title("Rumors of Free Public Transport Go Viral")
                .content("A viral post suggested the government will make public transport free starting September, but transport officials have denied any such plans.")
                .status(NewsStatus.FAKE)
                .reporter("Alex Nguyen")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-07T11:15:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/104/800/400")
                .build();
        n = newsRepository.save(n);

        // #5: Tech Company Launches Affordable Solar Panels
        n = News.builder()
                .title("Tech Company Launches Affordable Solar Panels")
                .content("SolarBright has announced a new line of solar panels that are 30% cheaper than current market options, aiming to boost renewable energy adoption.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("David Chan")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-06T08:00:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/107/800/400")
                .build();
        n = newsRepository.save(n);

        // #6: Mystery Animal Spotted in City Park
        n = News.builder()
                .title("Mystery Animal Spotted in City Park")
                .content("Several visitors claim to have seen an unknown animal in Greenleaf Park. Experts say the photos are inconclusive and could be digitally altered.")
                .status(NewsStatus.UNVERIFIED)
                .reporter("Emily Carter")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-05T17:55:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/106/800/400")
                .build();
        n = newsRepository.save(n);

        // #7: New Restaurant Claims to Serve Alien Cuisine
        n = News.builder()
                .title("New Restaurant Claims to Serve Alien Cuisine")
                .content("A downtown restaurant is advertising 'authentic alien cuisine' with dishes made from 'space ingredients.' Health inspectors are investigating the claims.")
                .status(NewsStatus.FAKE)
                .reporter("Lisa Wang")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-04T12:30:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/108/800/400")
                .build();
        n = newsRepository.save(n);

        // #8: Local Library Receives Million-Dollar Donation
        n = News.builder()
                .title("Local Library Receives Million-Dollar Donation")
                .content("The Central Library has received a $1.2 million donation from an anonymous benefactor to expand their digital resources and children's section.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Robert Johnson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-03T15:45:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/109/800/400")
                .build();
        n = newsRepository.save(n);

        // #9: Weather App Predicts Snow in August
        n = News.builder()
                .title("Weather App Predicts Snow in August")
                .content("A popular weather app is showing snow predictions for next week, causing confusion among users. Meteorologists say this is a technical glitch.")
                .status(NewsStatus.FAKE)
                .reporter("Maria Garcia")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-02T10:20:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/110/800/400")
                .build();
        n = newsRepository.save(n);

        // #10: City Announces Electric Bus Fleet Expansion
        n = News.builder()
                .title("City Announces Electric Bus Fleet Expansion")
                .content("The transportation department has announced plans to replace 50 diesel buses with electric models by the end of the year, reducing emissions by 40%.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("James Wilson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-08-01T14:15:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/111/800/400")
                .build();
        n = newsRepository.save(n);

        // #11: Viral Video Claims Time Travel Discovery
        n = News.builder()
                .title("Viral Video Claims Time Travel Discovery")
                .content("A viral video shows what appears to be a person from the future. Scientists have debunked the video as a sophisticated hoax using AI technology.")
                .status(NewsStatus.FAKE)
                .reporter("Tom Anderson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-31T18:30:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/112/800/400")
                .build();
        n = newsRepository.save(n);

        // #12: New Community Garden Opens Downtown
        n = News.builder()
                .title("New Community Garden Opens Downtown")
                .content("A community garden with 50 plots has opened in the heart of downtown, providing fresh produce and green space for urban residents.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Sarah Kim")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-30T09:00:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/113/800/400")
                .build();
        n = newsRepository.save(n);

        // #13: Social Media Post Claims Government Surveillance
        n = News.builder()
                .title("Social Media Post Claims Government Surveillance")
                .content("A widely shared post claims the government is monitoring all social media activity. Officials have denied these allegations as baseless.")
                .status(NewsStatus.FAKE)
                .reporter("Chris Martinez")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-29T16:45:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/114/800/400")
                .build();
        n = newsRepository.save(n);

        // #14: Local Artist Wins International Award
        n = News.builder()
                .title("Local Artist Wins International Award")
                .content("Local painter Elena Rodriguez has won the prestigious International Art Prize for her series on urban transformation and community resilience.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Jennifer Brown")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-28T11:20:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/115/800/400")
                .build();
        n = newsRepository.save(n);

        // #15: Rumor Spreads About Free Internet for All
        n = News.builder()
                .title("Rumor Spreads About Free Internet for All")
                .content("A message circulating online claims the city will provide free high-speed internet to all residents. City officials have confirmed this is false.")
                .status(NewsStatus.FAKE)
                .reporter("Kevin Davis")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-27T13:10:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/116/800/400")
                .build();
        n = newsRepository.save(n);

        // #16: New Recycling Program Launches Successfully
        n = News.builder()
                .title("New Recycling Program Launches Successfully")
                .content("The city's new comprehensive recycling program has diverted 60% of waste from landfills in its first month, exceeding initial projections.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Amanda White")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-26T08:30:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/117/800/400")
                .build();
        n = newsRepository.save(n);

        // #17: Alien Abduction Claims Surface Online
        n = News.builder()
                .title("Alien Abduction Claims Surface Online")
                .content("Multiple social media posts claim alien abductions occurred last night. Police have received no reports and experts dismiss these as hoaxes.")
                .status(NewsStatus.FAKE)
                .reporter("Daniel Taylor")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-25T20:15:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/118/800/400")
                .build();
        n = newsRepository.save(n);

        // #18: Local Hospital Receives Advanced MRI Machine
        n = News.builder()
                .title("Local Hospital Receives Advanced MRI Machine")
                .content("City General Hospital has installed a state-of-the-art MRI machine, reducing wait times for scans from 3 weeks to 3 days.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Rachel Green")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-24T14:50:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/119/800/400")
                .build();
        n = newsRepository.save(n);

        // #19: Viral Post Claims End of World Prediction
        n = News.builder()
                .title("Viral Post Claims End of World Prediction")
                .content("A viral post claims a famous psychic predicted the world will end next month. The psychic in question has denied making any such prediction.")
                .status(NewsStatus.FAKE)
                .reporter("Mark Thompson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-23T17:25:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/120/800/400")
                .build();
        n = newsRepository.save(n);

        // #20: New Bike Sharing Program Launches
        n = News.builder()
                .title("New Bike Sharing Program Launches")
                .content("The city has launched a bike sharing program with 200 bicycles available at 20 stations across downtown, promoting eco-friendly transportation.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Sophie Chen")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-22T10:40:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/121/800/400")
                .build();
        n = newsRepository.save(n);

        // #21: Fake News About Celebrity Death Circulates
        n = News.builder()
                .title("Fake News About Celebrity Death Circulates")
                .content("Social media posts claiming a famous actor has died are spreading rapidly. The actor's representatives have confirmed they are alive and well.")
                .status(NewsStatus.FAKE)
                .reporter("Laura Miller")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-21T19:30:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/122/800/400")
                .build();
        n = newsRepository.save(n);

        // #22: Local Startup Wins Innovation Award
        n = News.builder()
                .title("Local Startup Wins Innovation Award")
                .content("Tech startup GreenSolutions has won the National Innovation Award for their water purification technology that helps rural communities.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Peter Jackson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-20T12:15:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/123/800/400")
                .build();
        n = newsRepository.save(n);

        // #23: Hoax About Free Gasoline Spreads
        n = News.builder()
                .title("Hoax About Free Gasoline Spreads")
                .content("A message claiming gas stations will give free gasoline to all customers tomorrow is circulating. Gas station owners have denied this claim.")
                .status(NewsStatus.FAKE)
                .reporter("Nicole Adams")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-19T15:20:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/124/800/400")
                .build();
        n = newsRepository.save(n);

        // #24: New Public Wi-Fi Network Installed
        n = News.builder()
                .title("New Public Wi-Fi Network Installed")
                .content("The city has installed free public Wi-Fi in all major parks and downtown areas, providing internet access to residents and visitors.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Carlos Rodriguez")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-18T09:45:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/125/800/400")
                .build();
        n = newsRepository.save(n);

        // #25: Fake Lottery Winner Announcement
        n = News.builder()
                .title("Fake Lottery Winner Announcement")
                .content("Social media posts claim someone won a $50 million lottery jackpot in our city. Lottery officials confirm no such winning ticket was sold.")
                .status(NewsStatus.FAKE)
                .reporter("Hannah Lee")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-17T21:10:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/126/800/400")
                .build();
        n = newsRepository.save(n);

        // #26: Local Food Bank Receives Major Donation
        n = News.builder()
                .title("Local Food Bank Receives Major Donation")
                .content("A local food bank has received a $500,000 donation from a corporate foundation, allowing them to serve 10,000 more families monthly.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Michael Scott")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-16T11:30:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/127/800/400")
                .build();
        n = newsRepository.save(n);

        // #27: Viral Post Claims Government Secret Plan
        n = News.builder()
                .title("Viral Post Claims Government Secret Plan")
                .content("A viral post claims the government has a secret plan to control the weather. Meteorologists and officials have dismissed this as conspiracy theory.")
                .status(NewsStatus.FAKE)
                .reporter("Jessica Wong")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-15T16:55:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/128/800/400")
                .build();
        n = newsRepository.save(n);

        // #28: New Community Center Opens
        n = News.builder()
                .title("New Community Center Opens")
                .content("A new community center with fitness facilities, meeting rooms, and youth programs has opened in the north district, serving 5,000 residents.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Andrew Clark")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-14T08:20:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/129/800/400")
                .build();
        n = newsRepository.save(n);

        // #29: Hoax About Free College Education
        n = News.builder()
                .title("Hoax About Free College Education")
                .content("A message circulating claims all college education will be free starting next year. Education officials have confirmed this is not true.")
                .status(NewsStatus.FAKE)
                .reporter("Stephanie Moore")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-13T14:40:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/130/800/400")
                .build();
        n = newsRepository.save(n);

        // #30: Local Restaurant Wins National Award
        n = News.builder()
                .title("Local Restaurant Wins National Award")
                .content("Downtown restaurant 'Taste of Home' has won the National Restaurant Association's Best New Restaurant award for their innovative fusion cuisine.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Ryan Johnson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-12T12:05:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/131/800/400")
                .build();
        n = newsRepository.save(n);

        // #31: Fake News About Water Contamination
        n = News.builder()
                .title("Fake News About Water Contamination")
                .content("Social media posts claim the city's water supply is contaminated with dangerous chemicals. Water department officials have confirmed the water is safe.")
                .status(NewsStatus.FAKE)
                .reporter("Melissa Turner")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-11T18:25:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/132/800/400")
                .build();
        n = newsRepository.save(n);

        // #32: New Solar Farm Completed
        n = News.builder()
                .title("New Solar Farm Completed")
                .content("A 50-acre solar farm has been completed on the city's outskirts, generating enough electricity to power 3,000 homes and reducing carbon emissions.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Brian Wilson")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-10T10:15:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/133/800/400")
                .build();
        n = newsRepository.save(n);

        // #33: Viral Post Claims Time Machine Invention
        n = News.builder()
                .title("Viral Post Claims Time Machine Invention")
                .content("A viral post claims a local scientist has invented a working time machine. The scientist mentioned has denied any such invention or claims.")
                .status(NewsStatus.FAKE)
                .reporter("Diana Foster")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-09T20:50:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/134/800/400")
                .build();
        n = newsRepository.save(n);

        // #34: New Public Art Installation Unveiled
        n = News.builder()
                .title("New Public Art Installation Unveiled")
                .content("A stunning public art installation featuring recycled materials has been unveiled in the city square, created by local artist collective 'Green Dreams'.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Gregory Harris")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-08T13:35:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/135/800/400")
                .build();
        n = newsRepository.save(n);

        // #35: Hoax About Free Smartphones
        n = News.builder()
                .title("Hoax About Free Smartphones")
                .content("A message claims all residents will receive free smartphones from the government. Officials have confirmed this is completely false.")
                .status(NewsStatus.FAKE)
                .reporter("Ashley Cooper")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-07T17:00:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/136/800/400")
                .build();
        n = newsRepository.save(n);

        // #36: Local Charity Breaks Fundraising Record
        n = News.builder()
                .title("Local Charity Breaks Fundraising Record")
                .content("Local charity 'Help for All' has broken their annual fundraising record, raising $2.3 million to support homeless families and children.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Thomas Baker")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-06T11:45:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/137/800/400")
                .build();
        n = newsRepository.save(n);

        // #37: Fake News About Alien Contact
        n = News.builder()
                .title("Fake News About Alien Contact")
                .content("Social media posts claim government officials have made contact with aliens. Government spokespersons have denied all such claims.")
                .status(NewsStatus.FAKE)
                .reporter("Victoria Lewis")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-05T19:20:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/138/800/400")
                .build();
        n = newsRepository.save(n);

        // #38: New Electric Vehicle Charging Network
        n = News.builder()
                .title("New Electric Vehicle Charging Network")
                .content("The city has installed 100 new electric vehicle charging stations across the metropolitan area, supporting the growing number of EV owners.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Nathan Roberts")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-04T09:30:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/139/800/400")
                .build();
        n = newsRepository.save(n);

        // #39: Viral Post Claims Miracle Cure Discovery
        n = News.builder()
                .title("Viral Post Claims Miracle Cure Discovery")
                .content("A viral post claims scientists have discovered a miracle cure for all diseases. Medical experts have debunked this as completely false.")
                .status(NewsStatus.FAKE)
                .reporter("Olivia Martinez")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-03T15:10:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/140/800/400")
                .build();
        n = newsRepository.save(n);

        // #40: New Urban Farm Project Launches
        n = News.builder()
                .title("New Urban Farm Project Launches")
                .content("An urban farm project has launched in the city center, growing fresh vegetables on rooftops and providing jobs for local residents.")
                .status(NewsStatus.NOT_FAKE)
                .reporter("Patrick Sullivan")
                .reportedAt(LocalDateTime.ofInstant(Instant.parse("2025-07-02T12:25:00Z"), ZoneId.systemDefault()))
                .imageUrl("https://picsum.photos/id/141/800/400")
                .build();
        n = newsRepository.save(n);
    }
}
