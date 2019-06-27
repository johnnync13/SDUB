from django.conf.urls import url, handler404, handler500
from django.contrib.auth.views import login, logout
from forkilla import views as views

"""listOfAddresses = ["https://sd2019-forkillab1.herokuapp.com/forkilla/","https://sd2019-forkillab2.herokuapp.com/forkilla/", "https://sd2019-forkilla-b3.herokuapp.com","https://sd2019-forkilla-b4.herokuapp.com"
                   ,"https://sd2019-forkilla-b5.herokuapp.com","https://sd2019-forkilla-b6.herokuapp.com/forkilla/","http://sd2019-forkillab-7.herokuapp.com/forkilla/","https://sd2019-forkillab8.herokuapp.com","https://sd2019-forkillab9.herokuapp.com",
                   "https://sd2019-forkillab10.herokuapp.com","https://sd2019-forkillab11.herokuapp.com/forkilla","https://sd2019-forkilla-b-12.herokuapp.com/forkilla"]
"""
listOfAddresses = ["161.116.56.65","161.116.56.165"]
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^base/$', views.base, name='base'),
    url(r'^restaurants/$', views.restaurants, name='restaurants'),
    url(r'^restaurants/(?P<city>.*)/$', views.restaurants, name='restaurants'),
    url(r'^reservation/$', views.reservation, name='reservation'),
    url(r'^restaurant/(?P<number>.*)/$', views.details, name='detail'),
    url(r'^restaurants/(?P<city>.*)/(?P<category>.*)$', views.restaurants, name='restaurants'),
    url(r'^checkout/$', views.checkout, name='checkout'),
    url(r'^review/(?P<number>.*)/$', views.review, name='review'),
    url(r'^reservationlist/$', views.reservationlist, name='reservationlist'),
    url(r'^accounts/login/$', login, name='login'),
    url(r'^accounts/logout/$', logout, {'next_page': '/'}, name='logout'),
    url(r'^register/$', views.register, name='register'),
    url(r'^comparator$', views.comparator, {'ips': listOfAddresses}, name="comparator"),
]

