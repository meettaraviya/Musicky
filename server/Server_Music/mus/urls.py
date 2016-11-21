from django.conf.urls import url

from . import views

app_name = 'mus'
urlpatterns = [
	url(r'getlist/', views.getPreferenceList, name='songlist'),
	url(r'rec/', views.recommend, name='rec'),
	url(r'rate/', views.rate, name='rate'),
]
