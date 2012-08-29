package service;

import testing.service.CfuTvService;
import testing.service.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 17-08-12
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class CfuTvServiceTest extends PersistenceTestCase {
    private CfuTvService requestService;

    public void testInsert() throws ServiceException{ /*
        requestService = new CfuTvService();
        RitzauProgram request = new RitzauProgram();
        request.setChannel_name("dr1");
        request.setStarttid(new Date(22, 1, 0));
        request.setSluttid(new Date(23, 1, 0));
        
        
        request.setTitel("9 o'clock news");
        request.setKortomtale("It was a slow day.");
        requestService.insert(request);
        RitzauProgram request2 = new RitzauProgram();
        request2.setChannel_name("tv2");
        request2.setStarttid(new Date(25,1,0));
        request2.setSluttid(new Date(26,1,0));
        
        
        request2.setTitel("Action movie");
        request2.setKortomtale("Testitest");
        requestService.insert(request2);*/
    }

    public void testGetAllRequests() throws ServiceException{   /*
        requestService = new CfuTvService();
        RitzauProgram request = new RitzauProgram();
        request.setChannel_name("dr1");
        request.setStarttid(new Date(22, 1, 0));
        request.setSluttid(new Date(23, 1, 0));
        
        
        request.setTitel("9 o'clock news");
        request.setKortomtale("It was a slow day.");
        requestService.insert(request);
        RitzauProgram request2 = new RitzauProgram();
        request2.setChannel_name("tv2");
        request2.setStarttid(new Date(25,1,0));
        request2.setSluttid(new Date(26,1,0));
        
        
        request2.setTitel("Action movie");
        request2.setKortomtale("Testitest");
        requestService.insert(request2);

        System.out.println();
        System.out.println("-------------------------------Top-----------------------------");
        List<RitzauProgram> requests = requestService.getTop(100);
        assertEquals("Expect to find that we have created one request, not " + requests.size() , 2, requests.size());
        for(RitzauProgram cr : requests){
            System.out.println(cr.getId() + " | " + cr.getChannel_name() + " | " + cr.getTitel() + " | " + cr.getStarttid() + " | " + cr.getSluttid() + " | " + cr.getKortomtale());
        }       */
    }

    public void testGetByChannel() throws ServiceException{  /*
        requestService = new CfuTvService();
        RitzauProgram request = new RitzauProgram();
        request.setChannel_name("dr1");
        request.setStarttid(new Date(22, 1, 0));
        request.setSluttid(new Date(23, 1, 0));
        
        
        request.setTitel("9 o'clock news");
        request.setKortomtale("It was a slow day.");
        requestService.insert(request);
        RitzauProgram request2 = new RitzauProgram();
        request2.setChannel_name("tv2");
        request2.setStarttid(new Date(25,1,0));
        request2.setSluttid(new Date(26,1,0));
        
        
        request2.setTitel("Action movie");
        request2.setKortomtale("Testitest");
        requestService.insert(request2);

        System.out.println();
        System.out.println("-------------------------------Channel-------------------------");
        List<RitzauProgram> requests = requestService.getByChannel("dr1");
        assertEquals("Expect to find one request, not " + requests.size() , 1, requests.size());
        for(RitzauProgram cr : requests){
            System.out.println(cr.getId() + " | " + cr.getChannel_name() + " | " + cr.getTitel() + " | " + cr.getStarttid() + " | " + cr.getSluttid() + " | " + cr.getKortomtale());
        }      */
    }

    public void testGetByTitle() throws ServiceException{ /*
        requestService = new CfuTvService();
        RitzauProgram request = new RitzauProgram();
        request.setChannel_name("dr1");
        request.setStarttid(new Date(22, 1, 0));
        request.setSluttid(new Date(23, 1, 0));
        
        
        request.setTitel("9 o'clock news");
        request.setKortomtale("It was a slow day.");
        requestService.insert(request);
        RitzauProgram request2 = new RitzauProgram();
        request2.setChannel_name("tv2");
        request2.setStarttid(new Date(25,1,0));
        request2.setSluttid(new Date(26,1,0));
        
        
        request2.setTitel("Action movie");
        request2.setKortomtale("Testitest");
        requestService.insert(request2);

        System.out.println();
        System.out.println("------------------------------Title----------------------------");
        List<RitzauProgram> requests = requestService.getByTitle("Action movie");
        assertEquals("Expect to find one request, not " + requests.size() , 1, requests.size());
        for(RitzauProgram cr : requests){
            System.out.println(cr.getId() + " | " + cr.getChannel_name() + " | " + cr.getTitel() + " | " + cr.getStarttid() + " | " + cr.getSluttid() + " | " + cr.getKortomtale());
        }  */
    }

    public void testGetByTime() throws ServiceException {   /*
        requestService = new CfuTvService();
        RitzauProgram request = new RitzauProgram();
        request.setChannel_name("dr1");
        request.setStarttid(new Date(22, 1, 0));
        request.setSluttid(new Date(23, 1, 0));
        request.setTitel("9 o'clock news");
        request.setKortomtale("It was a slow day.");
        requestService.insert(request);
        RitzauProgram request2 = new RitzauProgram();
        request2.setChannel_name("tv2");
        request2.setStarttid(new Date(25,1,0));
        request2.setSluttid(new Date(26,1,0));
        request2.setTitel("Action movie");
        request2.setKortomtale("Testitest");
        requestService.insert(request2);

        System.out.println();
        System.out.println("------------------------------Time-----------------------------");
        List<RitzauProgram> requests = requestService.getByTime(new Date(21, 1, 0),new Date(24,1,0));
        assertEquals("Expect to find one request, not " + requests.size() , 1, requests.size());
        for(RitzauProgram rp : requests){
            System.out.println(rp.getId() + " | " + rp.getChannel_name() + " | " + rp.getTitel() + " | " + rp.getStarttid() + " | " + rp.getSluttid() + " | " + rp.getKortomtale());
        }    */
    }


    public void testXML() throws Exception{   /*
        requestService = new CfuTvService();
        RitzauProgram request = new RitzauProgram();
        request.setChannel_name("dr1");
        request.setSluttid(new Date(22, 1, 0));
        request.setStarttid(new Date(23, 1, 0));
        request.setTitel("9 o'clock news");
        request.setKortomtale("It was a slow day.");
        requestService.insert(request);
        RitzauProgram request2 = new RitzauProgram();
        request2.setChannel_name("tv2");
        request2.setSluttid(new Date(25, 1, 0));
        request2.setStarttid(new Date(26, 1, 0));
        request2.setTitel("Action movie");
        request2.setKortomtale("Testitest");
        requestService.insert(request2);

        List<RitzauProgram> requests = requestService.getTop(100);

        CfuTvService.write(requests, "C:\\Users\\asj\\Desktop\\JerseyTest\\RitzauProgram.xml");

        List<RitzauProgram> readRequests = CfuTvService.read("C:\\Users\\asj\\Desktop\\JerseyTest\\RitzauProgram.xml");

        for(int i = 0; i < readRequests.size(); i++){
            RitzauProgram rp = readRequests.get(i);
            RitzauProgram counter = requests.get(i);
            assertEquals(rp.getId(),counter.getId());
            assertEquals(rp.getKortomtale(),counter.getKortomtale());
            assertEquals(rp.getStarttid(),counter.getStarttid());
            assertEquals(rp.getSluttid(),counter.getSluttid());
            assertEquals(rp.getTitel(),counter.getTitel());
            assertEquals(rp.getChannel_name(),counter.getChannel_name());
        }       */
    }
}
