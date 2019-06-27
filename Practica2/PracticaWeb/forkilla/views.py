# Create your views here.
"""from django.http import HttpResponse
from .models import Restaurant
def index(request):
    return HttpResponse("Hello, world. You're at the Forkilla home page.")

def restaurants(request, city):
    if city:
        response = "You're looking at Restaurants from %s city" % city
    else:
        response = "You're looking featured Restaurants"
    return HttpResponse(response)

def restaurants(request, city=""):
    if city:
        response = "You're looking at Restaurants from %s <BR>" % city
        list_of_restaurants = Restaurant.objects.filter(city__iexact=city)
        response = response + '<BR> <li>' + '<BR> <li>'.join([str(restaurant) for restaurant in list_of_restaurants])
    else:
        response = "You're looking featured Restaurants"

    return HttpResponse(response)"""

from django.contrib.auth.models import User, Group
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from .serializers import RestaurantSerializer
from django.http import HttpResponse, HttpResponseRedirect
from django.urls import reverse
from django.shortcuts import render
from django.contrib.auth.forms import UserCreationForm
from .forms import ReservationForm
from .models import Restaurant, RestaurantInsertDate, ViewedRestaurants, Reservation, Review, User
from datetime import datetime
from django.contrib.auth.decorators import login_required, user_passes_test
from forkilla.permissions import CustomPermissions


def index(request):
    restaurants_by_city = Restaurant.objects.filter(is_promot="True")
    promoted = True
    context = {
        'restaurants': restaurants_by_city,
        'restaurants1': [(x, str(x.featured_photo)[15:]) for x in restaurants_by_city],
        'promoted': promoted,
        'viewedrestaurants': _check_session(request)
    }
    return render(request, 'forkilla/restaurants.html', context)


def restaurants(request, city="", category=""):
    promoted = False
    if city or category:
        if city:
            restaurants_by_city = Restaurant.objects.filter(city__iexact=city)
        if category:
            restaurants_by_city = Restaurant.objects.filter(city__iexact=city).order_by('category')
    else:
        restaurants_by_city = Restaurant.objects.filter(is_promot="True")
        promoted = True

    if request.method == "GET":
        dictionary = request.GET.dict()
        city = dictionary.get("city")
        if city:
            restaurants_by_city = Restaurant.objects.filter(city__iexact=city)
    context = {
        'city': city,
        'category': category,
        'restaurants': restaurants_by_city,
        'restaurants1': [(x, str(x.featured_photo)[15:]) for x in restaurants_by_city],
        'promoted': promoted,
        'viewedrestaurants': _check_session(request)
    }
    return render(request, 'forkilla/restaurants.html', context)


def details(request, number=""):
    viewedrestaurants = _check_session(request)
    restaurant = Restaurant.objects.get(restaurant_number=number)
    reviews = Review.objects.filter(restaurant=restaurant)
    lastviewed = RestaurantInsertDate(viewedrestaurants=viewedrestaurants, restaurant=restaurant)
    lastviewed.save()
    name = restaurant.name
    restaurant_number = restaurant.restaurant_number
    menu_description = restaurant.menu_description
    price_average = restaurant.price_average
    is_promot = restaurant.is_promot
    rate = restaurant.rate
    address = restaurant.address
    city = restaurant.city
    country = restaurant.country
    featured_photo = restaurant.featured_photo
    category = restaurant.category
    context = {
        'restaurant': restaurant,
        'restaurant_number': restaurant_number,
        'name': name,
        'menu_description': menu_description,
        'price_average': price_average,
        'is_promot': is_promot,
        'rate': rate,
        'address': address,
        'city': city,
        'country': country,
        'featured_photo': str(featured_photo)[15:],
        'category': category,
        'restaurant': restaurant,
        'reviews': reviews,
        'viewedrestaurants': viewedrestaurants
    }

    return render(request, 'forkilla/details.html', context)


def reservation(request):
    try:
        if request.method == "POST":
            form = ReservationForm(request.POST)
            if form.is_valid():
                resv = form.save(commit=False)
                restaurant_number = request.session["reserved_restaurant"]
                resv.restaurant = Restaurant.objects.get(restaurant_number=restaurant_number)
                request.session["reservation"] = resv.id
                request.session["result"] = "OK"
                resv.user = request.user
            else:
                request.session["result"] = form.errors

            dateFormat = datetime.strptime(request.POST['day'], "%d/%m/%Y").strftime('%Y-%m-%d')
            capacidad = 0
            for i in Reservation.objects.filter(time_slot=request.POST['time_slot'], day=dateFormat):
                capacidad += i.num_people
            if (resv.restaurant.capacity > capacidad + int(request.POST['num_people'])):
                resv.save()
            else:
                request.session["result"] = "No quedan sitios disponibles"
            return HttpResponseRedirect(reverse('checkout'))

        elif request.method == "GET":
            restaurant_number = request.GET["reservation"]
            restaurant = Restaurant.objects.get(restaurant_number=restaurant_number)
            request.session["reserved_restaurant"] = restaurant_number

            form = ReservationForm()
            context = {
                'restaurant': restaurant,
                'form': form,
                'viewedrestaurants': _check_session(request)
            }
    except Restaurant.DoesNotExist:
        return HttpResponse("Restaurant Does not exists")
    viewedrestaurants = _check_session(request)
    restaurant = Restaurant.objects.get(restaurant_number=request.session['reserved_restaurant'])
    lastviewed = RestaurantInsertDate(viewedrestaurants=viewedrestaurants, restaurant=restaurant)
    lastviewed.save()
    return render(request, 'forkilla/reservation.html', context)


def _check_session(request):
    if "viewedrestaurants" not in request.session:
        viewedrestaurants = ViewedRestaurants()
        viewedrestaurants.save()
        request.session["viewedrestaurants"] = viewedrestaurants.id_vr
    else:
        viewedrestaurants = ViewedRestaurants.objects.get(id_vr=request.session["viewedrestaurants"])
    return viewedrestaurants


def checkout(request):
    result = request.session.get("result")
    context = {
        'result': result,
        'viewedrestaurants': _check_session(request)
    }
    # request.session.clear()
    return render(request, 'forkilla/checkout.html', context)


def base(request):
    return render(request, 'forkilla/base.html')


def review(request, number=""):
    if request.method == "POST":
        restaurant = Restaurant.objects.get(restaurant_number=number)
        lista = [a for a, b in Review.STARS if b == int(request.POST["star"])]
        review = Review()
        review.restaurant = restaurant
        review.comment = request.POST["comment"]
        review.value_stars = lista[0]
        review.user = request.user
        review.save()
        return HttpResponseRedirect(reverse('detail', kwargs={"number": number}))
    context = {
        'restaurant_number': number,
        'viewedrestaurants': _check_session(request)

    }
    return render(request, 'forkilla/review.html', context)


def register(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            new_user = form.save()
            return HttpResponseRedirect(reverse("index"))
    else:
        form = UserCreationForm()
    return render(request, "registration/register.html", {
        'form': form,
    })


def reservationlist(request):
    reservations_by_user = Reservation.objects.filter(user=request.user).order_by('day')
    if request.method == "POST":
        reservation = Reservation.objects.filter(id=request.POST["ayuda"])
        reservation.delete()
    context = {
        'reservations': reservations_by_user,
        'viewedrestaurants': _check_session(request),
        'actualDate' : datetime.now()
    }
    return render(request, 'forkilla/reservationlist.html', context)


def user_can_download(user):
    return user.is_authenticated() and user.have_pay_card()


@user_passes_test(user_can_download, login_url="/login/")
def buy(request):
    # Code here can assume a logged-in user with the correct permission.
    ...
    return 0


def error404(request):
    data = {}
    return render(request, 'forkilla/404.html', data)


def error500(request):
    data = {}
    return render(request, 'forkilla/500.html', data)

def comparator(request, ips):
    context = {
        'ips':ips
    }
    return render(request, 'forkilla/comparator.html', context)

def user_like_commercial(user):
    return (user.groups.filter(name='Commercial').exists() or user.is_staff)


def user_like_admin(user):
    return user.is_staff


class RestaurantViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows Restaurants to be viewed or edited.
    """
    queryset = Restaurant.objects.all().order_by('category')
    serializer_class = RestaurantSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,CustomPermissions)

    def get_queryset(self):
        queryset = Restaurant.objects.all().order_by('restaurant_number')
        category = self.request.query_params.get('category', None)
        if category:
            queryset = queryset.filter(category=category)
        city = self.request.query_params.get('city', None)
        if city:
            queryset = queryset.filter(city=city)
        price = self.request.query_params.get('price', None)
        if price:
            queryset = queryset.filter(price_average=price)
        return queryset
