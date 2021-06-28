# Scorp Task

#Technologies and Terminologies

    MVVM architecture

    Live Data
    
    Hilt
    
    Kotlin

    Paging
    
    Binding


#Structure (Implementation and decision explained in comments)

    MainActivity
        Hosts fragment container and toolbar
    
    MainBaseFragment
        Handles interaction between activity and fragment
        
    MainFragment
        Observe user interaction and request responses that is given by MainViewModel
        
    MainViewModel
        Makes data manupulation
    
    MainRepository
        Api calls,In this case fetch from data source
        
    ApplicationModule
        Singleton provider

    UserListAdapter
        PagedListAdapter for recyclerview

    DataSourceHelper that implemented by DataSourceHelperImpl
        Has fetch method that fetches data from given DataSource.
        Provided singleton from ApplicationModule

    UserListDataSource
        Is a PageKeyedDataSource for pagination

     UserListDataSourceFactory
        As is creates UserListDataSource
    
    Also classes Response and Status implemented as clarified
    
    FragmentUtils implemented for fragment transaction

    CirclePorgressBar is a custom ui
# scorptask
