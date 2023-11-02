from app import constants as c
from app.classes.cashier import Cashier
from app.simulation.shop_dao import ShopDao
from app.simulation.simulation import Simulation

if __name__ == '__main__':
    print('--------------------POS SYSTEM SIMULATION | DESIGN PATTERNS SECOND ASSIGNMENT--------------------\n')

    dao = ShopDao()

    sim = Simulation(dao)
    sim.run()
