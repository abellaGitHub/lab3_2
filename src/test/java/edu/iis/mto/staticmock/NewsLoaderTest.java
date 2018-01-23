package edu.iis.mto.staticmock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    private NewsLoader newsLoader;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        Configuration configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        String readerType = "test";
        Whitebox.setInternalState(configuration, "readerType", readerType);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        IncomingNews incomingNews = new IncomingNews();
        IncomingInfo infoA = new IncomingInfo("infoA", SubsciptionType.A);
        IncomingInfo infoB = new IncomingInfo("infoB", SubsciptionType.B);
        IncomingInfo infoC = new IncomingInfo("infoC", SubsciptionType.C);
        IncomingInfo infoNone = new IncomingInfo("infoNone", SubsciptionType.NONE);
        incomingNews.add(infoA);
        incomingNews.add(infoB);
        incomingNews.add(infoC);
        incomingNews.add(infoNone);
        NewsReader newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);
        mockStatic(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReader);
    }

    @Test
    public void testReturnOnePublicInfo() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> publicContent = Whitebox.getInternalState(publishableNews, "publicContent");
        assertThat(publicContent.size(), is(1));
    }

    @Test
    public void testReturnThreeSubscription() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> subscribentContent = Whitebox.getInternalState(publishableNews, "subscribentContent");
        assertThat(subscribentContent.size(), is(3));
    }
}
