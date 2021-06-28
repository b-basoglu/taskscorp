# Scorp Task

#Done in expected a work of 6-hours total.

#Technologies and Terminologies

    MVVM architecture

    Live Data
    
    Hilt
    
    Kotlin

    Binding


#Structure (Implementation and decision explained in comments)

    MainActivity
        Hosts fragment container and Collapsing toolbar
    
    MainBaseFragment
        Handles interaction between activity and fragment
        
    MainFragment
        Observe user interaction and request responses that is given by MainViewModel
        
    MainViewModel
        Makes data manupulation
    
    MainRepository
        Api calls
        
    ApplicationModule
        Singleton DataSource
    
    Network Constants 
        Constants related to api calls
    
    Also classes Response and Status implemented as clarified
    
    FragmentUtils implemented for fragment transaction

    CirclePorgressBar is a custom ui
# scorptask
