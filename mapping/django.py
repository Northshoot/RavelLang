from django.db import models

class Person(models.Model):
    time = models.DateTimeField()
    temperature = models.DecimalField()
    voltage = models.DecimalField()