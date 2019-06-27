# Register your models here.
from django.contrib import admin
from .models import Restaurant, Reservation, ViewedRestaurants, RestaurantInsertDate, Review

admin.site.register(Restaurant)
admin.site.register(Reservation)
admin.site.register(ViewedRestaurants)
admin.site.register(RestaurantInsertDate)
admin.site.register(Review)