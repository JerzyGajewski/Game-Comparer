Game-Comparer
App for comparing used games from shops, using spring boot, jsoup, thymefeaf.

This application is using jsoup and multithreads to scrap information about games from different shops. It has scheduler that updates games info, removes old games and add new ones. Application is working on interfaces and Factory pattern. You can easy add new shop to the list.

I've tryed using many proxies and add proxy rotator but connection was very unstable so i decidet to stay on single ip address and add timeouts, between scrapping to prevent blocking ip by shops.
